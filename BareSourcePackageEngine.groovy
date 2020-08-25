import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class BareSourcePackageEngine extends AbstractPackageEngine {

   public BaseSourcePackageEngine(String schema, Connection connection) {
      this.schema = schema;
      this.connection = connection;
   }

   String getName() {
      return "Packaging simple strategy from non-suffix to "+suffix;
   }

   String getTaskLabel() {
      return "[BaseSourcePackageEngine] ";
   }

   String archivePattern() {
      return Pattern.quote(packageName) + "-([^-]*)" + ".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

