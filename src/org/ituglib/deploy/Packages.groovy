#!/usr/bin/env groovy

import hudson.model.*;
import java.io.*;
import java.sql.*;

class Packages {
   String schema;
   Connection connection;
   
   public Packages(String schema, Connection connection) {
      this.schema = schema;
      this.connection = connection;
   }

   public int getKey(String packageName) {
      String SELECT_KEY = "SELECT package_key FROM "+schema+".packages WHERE package_name = ?";
      PreparedStatement statement = connection.prepareStatement(SELECT_KEY);
      statement.setString(1, packageName);
      ResultSet resultSet = statement.executeQuery();
      int packageKey = -1;
      while (resultSet.next()) {
        packageKey = resultSet.getInt(1);
      }
      resultSet.close();
      statement.close();
      return packageKey;
   }
}

