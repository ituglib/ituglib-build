#!/usr/bin/env groovy

import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;
import org.ituglib.deploy.AbstractPackageEngine;

package org.ituglib.deploy;

class BareSourcePackageEngine extends AbstractPackageEngine {

   public BaseSourcePackageEngine(String schema, Connection connection) {
      this.schema = schema;
      this.connection = connection;
   }

   String getName() {
      return "Packaging simple strategy from non-suffix to "+suffix;
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^-]*)" + ".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

