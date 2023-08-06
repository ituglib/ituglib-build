import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class PlatformVersionPackageEngine extends AbstractPackageEngine {

   boolean debug = System.getenv("DEBUG") != null;
   String specificVersion;

   public PlatformVersionPackageEngine() {
      if (debug)
         println "PlatformVersionPackageEngine loaded";
   }

   String getName() {
      return "Packaging platform release strategy from -release"+suffix+" to "+suffix;
   }

   String getTaskLabel() {
      return "[PlatformVersionPackageEngine] ";
   }

   String archivePattern() {
      specificVersion = System.getenv("VERSION");
      if (debug) {
         println "Specified VERSION as "+specificVersion;
      }
      return Pattern.quote(packageName) + "-(" + specificVersion + ")" + "-release"+suffix+".tar.(.*)";
   }

   boolean isChgrpFirst() {
      return false;
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

