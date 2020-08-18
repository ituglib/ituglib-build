#!/usr/bin/env groovy

package org.ituglib.deploy;

import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;
import org.ituglib.deploy.*;

abstract class AbstractPackageEngine {
   boolean debug = System.getenv("DEBUG") != null;
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
   String patternString = null;
   Pattern pattern = null;
   String schema;
   Connection connection;

   abstract String getName();

   abstract String archivePattern();

   abstract String targetPattern(String version);

   public AbstractPackageEngine() {
	echo "Cannot use default AbstractPackageEngine constructor";
	System.exit(1);
   }

   public AbstractPackageEngine(String basename) {
	this(baseName, null, null, null, null, null, null, null, null, null);
   }

   public AbstractPackageEngine(String basename, String prefix, String suffix,
		String nonstopExtensions, String dependencies) {
	this(baseName, prefix, suffix, destination, staging, type, url,
		nonstopExtensions, dependencies, null);
   }

   public AbstractPackageEngine(String basename, String prefix, String suffix,
		String destination, String staging, String type, String url,
		String nonstopExtensions, String dependencies,
		String readmeFile) {
	this.basename = basename;
	this.staging = staging;
	this.destination = destination;
	this.prefix = prefix;
	this.suffix = suffix;
	this.type = type;
	this.url = url;
	this.nonstopExtensions = nonstopExtensions;
	this.dependencies = dependencies;
	this.readmeFile = readmeFile;
	destinationDirectory = new File(destination);

	packageName = prefix == null ? basename : prefix + basename;
	workspace = System.getenv("WORKSPACE");

	this.patternString = archivePattern();
	this.pattern = Pattern.compile(patternString);
	File destinationDirectory = new File(destination);
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
         Matcher matcher = pattern.matcher(archive.getName());
         if (matcher.matches()) {
            packageCount++;
            String version = matcher.group(1);
            String compression = matcher.group(2);
            Repackage repackager = new Repackage(archive, version, compression);
            File temp = new File("/tmp/"+basename);
            repackager.decompress(temp);
            repackager.chown(temp);
            repackager.chgrp(temp);
            String newPatternString = targetPattern(version);
            repackager.setCompression("gz");
            File tarFile = new File(destinationDirectory, newPatternString);
            repackager.recompress(temp, tarFile);
            temp.delete();
      
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
               fileset.insertNewFile(fileKey, versionKey, directoryKey, type, tarFile);
            } else {
               println taskLabel+"Found "+tarFile.getName()+" in the database";
               fileset.updateExisting(fileKey, tarFile);
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
