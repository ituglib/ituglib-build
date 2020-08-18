#!/usr/bin/env groovy

import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;
import org.ituglib.deploy.AbstractPackageEngine;

class ReleasePackageEngine extends AbstractPackageEngine {

   public ReleasePackageEngine() {
   }

   String getName() {
      return "Packaging release strategy from -release suffix to "+suffix;
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^-]*)" + "-release.tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

