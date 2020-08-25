import hudson.model.*;
import java.io.*;
import java.sql.*;

class Versions {
   boolean debug = System.getenv("DEBUG") != null;
   String schema;
   Connection connection;
   String nonstopExtensions;
   String dependencies;
   String readmeFile;
   int readmeDirKeyValue = 0;
   
   public Versions(String schema, Connection connection) {
      this.schema = schema;
      this.connection = connection;
      this.nonstopExtensions;
      this.dependencies;
      this.readmeFile;
   }

   public int getKey(int packageKey, String version) {
      String SELECT_KEY = "SELECT version_key FROM "+schema+".versions WHERE package_key = ? and version_number = ?";
      PreparedStatement statement = connection.prepareStatement(SELECT_KEY);
      statement.setInt(1, packageKey);
      statement.setString(2, version);
      ResultSet resultSet = statement.executeQuery();
      int versionKey = -1;
      while (resultSet.next()) {
        versionKey = resultSet.getInt(1);
      }
      resultSet.close();
      statement.close();
      return versionKey;
   }

   public int getNewVersion() {
      String SELECT_KEY = "SELECT max(version_key)+1 FROM "+schema+".versions";
      PreparedStatement statement = connection.prepareStatement(SELECT_KEY);
      ResultSet resultSet = statement.executeQuery();
      int versionKey = -1;
      while (resultSet.next()) {
        versionKey = resultSet.getInt(1);
      }
      resultSet.close();
      statement.close();
      return versionKey;
   }

   public void insertNewVersion(int versionKey,
                                String packageName, int packageKey,
                                String version, String url) {
      String INSERT_VERSION = "INSERT INTO "+schema+".versions (version_key,package_key,version_name,version_number,url,NONSTOP_EXTENSIONS,DEPENDENCIES,README_FILE,README_DIR_KEY) VALUES (?,?,?,?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(INSERT_VERSION);
      statement.setInt(1,versionKey);
      statement.setInt(2,packageKey);
      statement.setString(3,packageName+"-"+version);
      statement.setString(4,version);
      if (url == null) {
         statement.setNull(5,Types.VARCHAR);
      } else {
         statement.setString(5,url);
      }
      if (nonstopExtensions == null) {
         statement.setNull(6,Types.VARCHAR);
      } else {
         statement.setString(6,nonstopExtensions);
      }
      if (dependencies == null) {
         statement.setNull(7,Types.VARCHAR);
      } else {
         statement.setString(7,dependencies);
      }
      if (readmeFile == null) {
         statement.setNull(8,Types.VARCHAR);
         statement.setNull(9,Types.INTEGER);
      } else {
         statement.setString(8,readmeFile);
         statement.setString(9,readmeDirKeyValue);
      }
      statement.executeUpdate();
      statement.close();
   }
   public void updateExistingVersion(int versionKey,
                                String packageName, int packageKey,
                                String version, String url) {
      String UPDATE_VERSION = "UPDATE "+schema+".versions SET version_name=?,version_number=?,url=?,NONSTOP_EXTENSIONS=?,DEPENDENCIES=?,README_FILE=?,README_DIR_KEY=? WHERE version_key=? AND package_key=?";
      PreparedStatement statement = connection.prepareStatement(UPDATE_VERSION);
      int i=1;
      statement.setString(i++,packageName+"-"+version);
      statement.setString(i++,version);
      if (url == null) {
         statement.setNull(i++,Types.VARCHAR);
      } else {
         statement.setString(i++,url);
      }
      if (nonstopExtensions == null) {
         statement.setNull(i++,Types.VARCHAR);
      } else {
         statement.setString(i++,nonstopExtensions);
      }
      if (dependencies == null) {
         statement.setNull(i++,Types.VARCHAR);
      } else {
         statement.setString(i++,dependencies);
      }
      if (readmeFile == null) {
         statement.setNull(i++,Types.VARCHAR);
         statement.setNull(i++,Types.INTEGER);
      } else {
         statement.setString(i++,readmeFile);
         statement.setString(i++,readmeDirKeyValue);
      }
      statement.setInt(i++,versionKey);
      statement.setInt(i++,packageKey);
      statement.executeUpdate();
      statement.close();
   }
   void setNonstopExtensions(String value) {
      nonstopExtensions = value;
      if (debug) {
         println "Extensions="+value;
      }
   }
   void setDependencies(String value) {
      dependencies = value;
      if (debug) {
         println "Dependencies="+value;
      }
   }
   void setReadmeFile(String value) {
      readmeFile = value;
      if (debug) {
         println "Readme="+value;
      }
   }
   void setReadmeDirKeyValue(int value) {
      readmeDirKeyValue = value;
      if (debug) {
         println "ReadmeDirKeyValue="+value;
      }
   }
}

