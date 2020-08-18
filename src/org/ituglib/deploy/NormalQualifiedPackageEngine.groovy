#!/usr/bin/env groovy

package org.ituglib.deploy;

import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;
import org.ituglib.deploy.AbstractPackageEngine;

class NormalQualifiedPackageEngine extends AbstractPackageEngine {

   public NormalQualifiedPackageEngine(String basename2, String prefix2,
		String suffix2, String nonstopExtensions2, String dependencies2,
		String readmeFile2) {
        super(baseName2, prefix2, suffix2, destination2, staging2, type2, url2,
                nonstopExtensions2, dependencies2, readmeFile2);
   }

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

