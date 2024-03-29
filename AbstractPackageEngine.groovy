import hudson.model.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.sql.*;
import java.util.regex.*;

abstract class AbstractPackageEngine {
   boolean debug = System.getenv("DEBUG") != null;
   boolean noUnstage = System.getenv("NOUNSTAGE") != null;
   String taskLabel = "[PackageEngine] ";
   String basename = System.getenv("BASENAME");
   String staging = System.getenv("DIST");
   String destination = System.getenv("DEST");
   File destinationDirectory = new File(destination);
   String prefix = System.getenv("PREFIX");
   String suffix = System.getenv("SUFFIX");
   String type = System.getenv("TYPE");
   String packageName = prefix == null ? basename : prefix + basename;
   String workspace = System.getenv("WORKSPACE");
   String url = System.getenv("URL");
   String nonstopExtensions = System.getenv("NONSTOP_EXTENSIONS");
   String dependencies = System.getenv("DEPENDENCIES");
   String readmeFile = System.getenv("README_FILE");
   int readmeDirKeyValue = 2; // Hard coded value for the readme folder
   String targetShell = String.format('sh package.stage.target', workspace);
   String patternString = null;
   Pattern pattern = null;
   String schema;
   Connection connection;

   abstract String getName();

   abstract String archivePattern();

   abstract String targetPattern(String version);

   abstract public getTaskLabel();
   
   abstract boolean isChgrpFirst();

   public AbstractPackageEngine() {
      this.patternString = archivePattern();
      if (debug) {
         println "Using pattern "+this.patternString;
      }
      this.pattern = Pattern.compile(patternString);
      File destinationDirectory = new File(destination);
      this.taskLabel = getTaskLabel();
   }

    private String bytesToHex(byte[] hash) {
      StringBuilder hexString = new StringBuilder(2 * hash.length);
      for (int i = 0; i < hash.length; i++) {
         String hex = Integer.toHexString(0xff & hash[i]);
         if(hex.length() == 1) {
            hexString.append('0');
         }
         hexString.append(hex);
      }
      return hexString.toString();
   }
 
   public AbstractRepackage getRepackager(File archive, String version, String compression) {
      return new Repackage(archive, version, compression);
   }

   public void setConnection(Connection connection) {
      this.connection = connection;
   }

   public void setSchema(String schema) {
      this.schema = schema;
   }

   public void execute() {
      println taskLabel+getName();

      int directoryKey = new Directories(schema, connection).getKey(destination);
      if (directoryKey < 0) {
         println taskLabel+"Cannot find directory "+destination+"\n";
         System.exit(1);
      }
      println taskLabel+"Found "+destination+" in the database";

      int packageKey = new Packages(schema, connection).getKey(packageName);
      if (packageKey < 0) {
         println taskLabel+"Cannot find package "+packageName+" in the database";
         System.exit(1);
      }
      println taskLabel+"Found "+packageName+" in the database";

      int packageCount = 0;

      for (File archive : new File(staging).listFiles()) {
         if (debug) {
            println "Checking "+archive.getName()+" for match with "+pattern;
         }
         Matcher matcher = pattern.matcher(archive.getName());
         if (matcher.matches()) {
            if (debug) {
               println "Matched";
            }
            packageCount++;
            String version = matcher.group(1);
            String compression = matcher.group(2);
            AbstractRepackage repackager = getRepackager(archive, version, compression);
            File temp = new File("/tmp/"+basename);
            repackager.decompress(temp);
            if (isChgrpFirst()) {
               println "Using chgrp first";
               repackager.chgrp(temp);
               repackager.chown(temp);
            } else {
               repackager.chown(temp);
               repackager.chgrp(temp);
            }
            String newPatternString = targetPattern(version);
            repackager.setCompression("gz");
            File tarFile = new File(destinationDirectory, newPatternString);
            repackager.recompress(temp, tarFile);
            temp.delete();
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] tarFileData = Files.readAllBytes(tarFile.toPath());
            String encodedHash = bytesToHex(digest.digest(tarFileData));

            Versions versions = new Versions(schema, connection);
            versions.setNonstopExtensions(nonstopExtensions);
            versions.setDependencies(dependencies);
            versions.setReadmeFile(readmeFile);
            if (readmeFile != null) {
               versions.setReadmeDirKeyValue(readmeDirKeyValue);
            }

            int versionKey = versions.getKey(packageKey, version);
            if (versionKey < 0) {
               versionKey = versions.getNewVersion();
               if (versionKey < 0) {
                  versionKey = 1;
                  println taskLabel+"Initial version key";
               } else {
                  println taskLabel+"New version detected for "+version;
               }
               versions.insertNewVersion(versionKey, packageName, packageKey, version, url);
               println taskLabel+"New version entry created for "+version;
            } else {
               versions.updateExistingVersion(versionKey, packageName, packageKey, version, url);
               println taskLabel+"Updated version "+version+" in the database";

            }
            
            FileSet fileset = new FileSet(schema, connection);
            int fileKey = fileset.getKey(versionKey, directoryKey, type, tarFile);
            if (fileKey < 0) {
               fileKey = fileset.getNewFile();
               if (fileKey < 0) {
                  fileKey = 1;
                  println taskLabel+"Initial file key"
               } else {
                  println taskLabel+"New file "+tarFile.getName();
               }
               fileset.insertNewFile(fileKey, versionKey, directoryKey, type, tarFile, encodedHash, "sha256");
            } else {
               println taskLabel+"Found "+tarFile.getName()+" in the database";
               fileset.updateExisting(fileKey, tarFile, encodedHash, "sha256");
            }
            connection.commit();
            repackager.clean(temp);
            if (noUnstage) {
		println "Kept "+archive.getName();
            } else {
                repackager.clean(archive);
            }
         }
      }
      if (packageCount < 1) {
         println taskLabel+"No work found\n";
         // System.exit(1);
      } else {
         println taskLabel+packageCount + " packages added";
      }
   }
}
