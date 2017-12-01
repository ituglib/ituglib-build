import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class PlatformPackageEngine extends AbstractPackageEngine {

   public PlatformPackageEngine() {
   }

   String getName() {
      return "Packaging platform release strategy from -release"+suffix+" to "+suffix;
   }

   String archivePattern() {
      return packageName + "-([^-]*)" + "-release"+suffix+".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

