import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class JdbcLoader {

   static private final String JDBC_MX_TAR =
      "/usr/tandem/jdbcMx/current/lib/jdbcMx.jar";
   static private final String JDBC_DRIVER = 
      "com.tandem.sqlmx.SQLMXDriver";

   public JdbcLoader() {
   }

   public Connection getConnection() {
      File localFile = new File(JDBC_MX_TAR);
      // Use the groovy script's classLoader to add the jar file at runtime.
      this.class.classLoader.rootLoader.addURL(localFile.toURI().toURL());

      // Now register the driver.
      Class.forName(JDBC_DRIVER);

      Connection connection = DriverManager.getConnection("jdbc:sqlmx:");
      connection.setAutoCommit(false);
      return connection;
   }
}

