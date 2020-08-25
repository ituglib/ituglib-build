import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class PlatformBugfixPackageEngine extends AbstractPackageEngine {

   boolean debug = System.getenv("DEBUG") != null;

   public PlatformBugfixPackageEngine() {
      if (debug)
         println "PlatformBugfixPackageEngine loaded";
   }

   String getName() {
      return "Packaging platform bugfix release strategy from 2-gitno-release"+suffix+" to "+suffix;
   }

   String getTaskLabel() {
      return "[PlatformBugfixPackageEngine] ";
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^s+]*)" + "-release"+suffix+".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

