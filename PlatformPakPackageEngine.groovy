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
      return "[PlatformPackageEngine] ";
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^s+-]*)" + "-release"+suffix+".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".pak.bin";
   }

   public AbstractRepackage getRepackager(File archive, String version, String c
ompression) {
      return new RepackagePak(archive, version, compression);
   }

}

