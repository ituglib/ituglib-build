import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class PlatformVersionPackageEngine extends AbstractPackageEngine {

   boolean debug = System.getenv("DEBUG") != null;
   String specificVersion = System.getenv("VERSION");

   public PlatformVersionPackageEngine() {
      if (debug)
         println "PlatformVersionPackageEngine loaded";
   }

   String getName() {
      return "Packaging platform release strategy from -release"+suffix+" to "+suffix;
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-(" + specificVersion + ")" + "-release"+suffix+".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

