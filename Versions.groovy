import hudson.model.*;
import java.io.*;
import java.sql.*;

class Versions {
   String schema;
   Connection connection;
   
   public Versions(String schema, Connection connection) {
      this.schema = schema;
      this.connection = connection;
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
      String INSERT_VERSION = "INSERT INTO "+schema+".versions (version_key,package_key,version_name,version_number,url,NONSTOP_EXTENSIONS,DEPENDENCIES) VALUES (?,?,?,?,?,NULL,NULL)";
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
      statement.executeUpdate();
      statement.close();
   }
}

