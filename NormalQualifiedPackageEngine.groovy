import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class NormalQualifiedPackageEngine extends AbstractPackageEngine {

   public NormalQualifiedPackageEngine() {
   }

   String getName() {
      return "Packaging simple with qualified version strategy preserving archive name";
   }

   String archivePattern() {
      return packageName + "-([^-]*-[^-]*)" + suffix + ".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

