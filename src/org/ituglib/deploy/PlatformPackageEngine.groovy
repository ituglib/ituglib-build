#!/usr/bin/env groovy

import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;
import org.ituglib.deploy.AbstractPackageEngine;

class PlatformPackageEngine extends AbstractPackageEngine {

   boolean debug = System.getenv("DEBUG") != null;

   public PlatformPackageEngine() {
      if (debug)
         println "PlatformPackageEngine loaded";
   }

   String getName() {
      return "Packaging platform release strategy from -release"+suffix+" to "+suffix;
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^-]*)" + "-release"+suffix+".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

