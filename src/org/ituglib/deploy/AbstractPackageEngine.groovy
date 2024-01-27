#!/usr/bin/env groovy

package org.ituglib.deploy;

import hudson.model.*;
import java.io.*;
java.nio.file.*;
import java.security.*;
import java.sql.*;
import java.util.regex.*;
import org.ituglib.deploy.*;

abstract class AbstractPackageEngine {
   boolean debug = System.getenv("DEBUG") != null;
   boolean dryRun = false;
   String taskLabel = "[PackageEngine] ";
   String basename;
   String staging;
   String destination;
   File destinationDirectory;
   String prefix;
   String suffix;
   String type;
   String packageName;
   String workspace;
   String url;
   String nonstopExtensions;
   String dependencies;
   String readmeFile;
   int readmeDirKeyValue = 2; // Hard coded value for the readme folder
   String targetShell = String.format('sh package.stage.target', workspace);
   String schema;
   Connection connection;

   abstract String getName();

   abstract String archivePattern();

   abstract String targetPattern(String version);

   public AbstractPackageEngine() {
	println "Abstract initialized";
	workspace = System.getenv("WORKSPACE");
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

   public void updateDryRun(boolean value) {
	this.dryRun = value;
   }

   public void updateStaging(String value) {
	this.staging = value;
   }

   public void updateSuffix(String value) {
	this.suffix = value;
   }

   public void updateType(String value) {
	this.type = value;
   }

   public void updateUrl(String value) {
	this.url = value;
   }

   public void updateDependencies(String value) {
	this.dependencies = value;
   }

   public void updateNonstopExtensions(String value) {
	this.nonstopExtensions = value;
   }

   public void updateReadmeFile(String value) {
	this.readmeFile = value;
   }

   public void updatePrefix(String value) {
	prefix = value;
	packageName = prefix == null ? basename : prefix + basename;
   }

   public void updateBasename(String value) {
	basename = value;
	packageName = prefix == null ? basename : prefix + basename;
   }

   public void updateDestination(String value) {
	this.destination = value;
	this.destinationDirectory = new File(destination);
   }

   public void updateConnection(Connection connection) {
      this.connection = connection;
   }

   public void updateSchema(String schema) {
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

      Pattern pattern = Pattern.compile(archivePattern());
      for (File archive : new File(staging).listFiles()) {
         Matcher matcher = pattern.matcher(archive.getName());
         if (matcher.matches()) {
            packageCount++;
            String version = matcher.group(1);
            String compression = matcher.group(2);
            Repackage repackager = new Repackage(archive, version, compression);
	    if (dryRun) {
		println "Would use "+archive;
		break;
	    }
            File temp = new File("/tmp/"+basename);
            repackager.decompress(temp);
            if (isChgrpFirst()) {
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
            versions.setReadmeDirKeyValue(readmeDirKeyValue);

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
               println taskLabel+"Found version "+version+" in the database";
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
            repackager.clean(archive);
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
