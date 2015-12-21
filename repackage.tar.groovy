import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

String schema = System.getenv("SCHEMA");
String basename = System.getenv("BASENAME");
String staging = System.getenv("DIST");
String destination = System.getenv("DEST");
String prefix = System.getenv("PREFIX");
String suffix = System.getenv("SUFFIX");
String type = System.getenv("TYPE");
String packageName = prefix == null ? basename : prefix + basename;
String workspace = System.getenv("WORKSPACE");
String url = System.getenv("URL");
String targetShell = String.format('sh package.stage.target',
                                    workspace);
String patternString = packageName + "-([^-]*)" + suffix + ".tar.(.*)";
Pattern pattern = Pattern.compile(patternString);
File destinationDirectory = new File(destination);

File localFile = new File("/usr/tandem/jdbcMx/current/lib/jdbcMx.jar");
// Use the groovy script's classLoader to add the jar file at runtime.
this.class.classLoader.rootLoader.addURL(localFile.toURI().toURL());

// Now register the driver.
Class.forName("com.tandem.sqlmx.SQLMXDriver");

Connection connection = DriverManager.getConnection("jdbc:sqlmx:");
connection.setAutoCommit(false);

int directoryKey = new Directories(schema, connection).getKey(destination);
if (directoryKey < 0) {
   println "Cannot find directory "+destination+"\n";
   System.exit(1);
}
println "Found "+destination;

int packageKey = new Packages(schema, connection).getKey(packageName);
if (packageKey < 0) {
   println "Cannot find package "+packageName;
   System.exit(1);
}
println "Found "+packageName;

for (File archive : new File(staging).listFiles()) {
   Matcher matcher = pattern.matcher(archive.getName());
   if (matcher.matches()) {
      String version = matcher.group(1);
      String compression = matcher.group(2);
      Repackage repackager = new Repackage(archive, version, compression);
      File temp = new File("/tmp/"+basename);
      repackager.decompress(temp);
      repackager.chown(temp);
      repackager.chgrp(temp);
      File tarFile = new File(destinationDirectory, archive.getName());
      repackager.recompress(temp, tarFile);
      temp.delete();

      Versions versions = new Versions(schema, connection);
      int versionKey = versions.getKey(packageKey, version);
      if (versionKey < 0) {
         versionKey = versions.getNewVersion();
         if (versionKey < 0) {
            versionKey = 1;
            println "Initial version key";
         } else {
            println "New version detected for "+version;
         }
         versions.insertNewVersion(versionKey, packageName, packageKey, version, url);
         println "New version entry created for "+version;
      } else {
         println "Found version "+version;
      }
      
      FileSet fileset = new FileSet(schema, connection);
      int fileKey = fileset.getKey(versionKey, directoryKey, type, tarFile);
      if (fileKey < 0) {
         fileKey = fileset.getNewFile();
         if (fileKey < 0) {
            fileKey = 1;
            println "Initial file key"
         } else {
            println "New file "+tarFile.getName();
         }
         fileset.insertNewFile(fileKey, versionKey, directoryKey, type, tarFile);
      } else {
         println "Found "+tarFile.getName();
         fileset.updateExisting(fileKey, tarFile);
      }
      connection.commit();
      repackager.clean(temp);
      repackager.clean(archive);
   }
}
connection.close();
