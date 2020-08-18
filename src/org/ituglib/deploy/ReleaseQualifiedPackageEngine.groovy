#!/usr/bin/env groovy

import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;
import org.ituglib.deploy.AbstractPackageEngine;

class ReleaseQualifiedPackageEngine extends AbstractPackageEngine {

   public ReleaseQualifiedPackageEngine() {
   }

   String getName() {
      return "Packaging release with qualified version strategy preserving archive name";
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^-]*-[^-]*)" + "-release.tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

