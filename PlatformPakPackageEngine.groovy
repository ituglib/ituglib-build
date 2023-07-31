import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class PlatformPakPackageEngine extends AbstractPackageEngine {

   boolean debug = System.getenv("DEBUG") != null;

   public PlatformPakPackageEngine() {
      if (debug)
         println "PlatformPakPackageEngine loaded";
   }

   String getName() {
      return "Packaging platform release strategy from -release"+suffix+" to "+suffix;
   }

   String getTaskLabel() {
      return "[PlatformPakPackageEngine] ";
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^s+-]*)" + "-release"+suffix+".pak.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".pak.bin";
   }

   boolean isChgrpFirst() {
      return true;
   }

   public AbstractRepackage getRepackager(File archive, String version, String compression) {
      return new RepackagePak(archive, version, compression);
   }

}

