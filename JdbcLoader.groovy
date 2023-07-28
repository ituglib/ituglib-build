import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class JdbcLoader {
   static boolean debug = System.getenv("DEBUG") != null;
   static private final String JDBC_MX_TAR =
      System.getenv("JDBC_MX_TAR") != null ?
         System.getenv("JDBC_MX_TAR")
         : "/usr/tandem/jdbcMx/T1275L38/lib/jdbcMx.jar";
   static private final String JDBC_DRIVER = 
      "com.tandem.sqlmx.SQLMXDriver";

   public JdbcLoader() {
   }

   public Connection getConnection() {
      if (debug) {
         println "CLASSPATH=" + System.getenv("CLASSPATH");
         println "JAVA_HOME=" + System.getenv("JAVA_HOME");
         println "_RLD_LIB_PATH=" + System.getenv("_RLD_LIB_PATH");
         println "PATH=" + System.getenv("PATH");
         println "JDBC_MX_TAR=" + JDBC_MX_TAR;
      }
      File localFile = new File(JDBC_MX_TAR);
      // Use the groovy script's classLoader to add the jar file at runtime.
      this.class.classLoader.rootLoader.addURL(localFile.toURI().toURL());
      if (debug) {
         println localFile.getAbsolutePath() + " has been loaded";
      }

      // Now register the driver.
      Class.forName(JDBC_DRIVER);
      if (debug) {
         println JDBC_DRIVER + " registered";
      }

      Connection connection = DriverManager.getConnection("jdbc:sqlmx:");
      if (debug) {
         println "jdbc:sqlmx: connected";
      }
      connection.setAutoCommit(false);
      return connection;
   }
}

