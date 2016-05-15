import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class ReleaseQualifiedPackageEngine extends AbstractPackageEngine {

   public ReleaseQualifiedPackageEngine() {
   }

   String getName() {
      return "Packaging release with qualified version strategy preserving archive name";
   }

   String archivePattern() {
      return packageName + "-([^-]*-[^-]*)" + "-release.tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

