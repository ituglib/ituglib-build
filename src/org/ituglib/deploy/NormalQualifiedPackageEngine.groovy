#!/usr/bin/env groovy

package org.ituglib.deploy;

import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;
import org.ituglib.deploy.AbstractPackageEngine;

class NormalQualifiedPackageEngine extends AbstractPackageEngine {

   String getName() {
      return "Packaging simple with qualified version strategy preserving archive name";
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^-]*-[^-]*)" + suffix + ".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

