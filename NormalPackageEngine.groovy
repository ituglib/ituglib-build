import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class NormalPackageEngine extends AbstractPackageEngine {

   public NormalPackageEngine() {
   }

   String getName() {
      return "Packaging simple strategy preserving archive name";
   }

   String getTaskLabel() {
      return "[NormalPackageEngine] ";
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^-]*)" + suffix + ".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }

   boolean isChgrpFirst() {
      return false;
   }
}

