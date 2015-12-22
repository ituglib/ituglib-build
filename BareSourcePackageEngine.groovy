import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class BareSourcePackageEngine extends AbstractPackageEngine {

   public BaseSourcePackageEngine(String schema, Connection connection) {
      this.schema = schema;
      this.connection = connection;
   }

   String archivePattern() {
      return packageName + "-([^-]*)" + ".tar.(.*)";
   }

   String targetPattern(String version) {
      return packageName + '-' + version + suffix + ".tar.gz";
   }
}

