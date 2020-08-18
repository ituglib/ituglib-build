#!/usr/bin/env groovy

import hudson.model.*;
import java.io.*;
import java.sql.*;

package org.ituglib.deploy;

class Directories {
   
   String schema;
   Connection connection;

   public Directories(String schema, Connection connection) {
      this.schema = schema;
      this.connection = connection;
   }

   public int getKey(String destination) {
      String SELECT_KEY = "SELECT directory_key FROM "+schema+".directories WHERE directory_path = ?";
      PreparedStatement statement = connection.prepareStatement(SELECT_KEY);
      statement.setString(1, destination);
      ResultSet resultSet = statement.executeQuery();
      int directoryKey = -1;
      while (resultSet.next()) {
        directoryKey = resultSet.getInt(1);
      }
      resultSet.close();
      statement.close();
      return directoryKey;
   }
}
