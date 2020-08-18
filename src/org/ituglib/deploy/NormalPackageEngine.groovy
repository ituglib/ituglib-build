#!/usr/bin/env groovy

import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;
import org.ituglib.deploy.AbstractPackageEngine;

package org.ituglib.deploy;

class NormalPackageEngine extends AbstractPackageEngine {

   public NormalPackageEngine() {
   }

   String getName() {
      return "Packaging simple strategy preserving archive name";
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^-]*)" + suffix + ".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

