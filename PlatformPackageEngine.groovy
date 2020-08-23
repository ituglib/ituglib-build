import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

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
      return Pattern.quote(packageName) + "-([^s+-]*)" + "-release"+suffix+".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

