import hudson.model.*;
import java.io.*;
import java.sql.*;

class FileSet {
   String schema;
   Connection connection;
   
   public FileSet(String schema, Connection connection) {
      this.schema = schema;
      this.connection = connection;
   }

   public int getKey(int versionKey, int directoryKey, String fileType, File archive) {
      String SELECT_KEY = "SELECT file_key FROM "+schema+".files WHERE version_key = ? AND directory_key = ? AND file_type = ? AND file_name = ?";
      PreparedStatement statement = connection.prepareStatement(SELECT_KEY);
      statement.setInt(1, versionKey);
      statement.setInt(2, directoryKey);
      statement.setString(3, fileType);
      statement.setString(4, archive.getName());
      ResultSet resultSet = statement.executeQuery();
      int fileKey = -1;
      while (resultSet.next()) {
        fileKey = resultSet.getInt(1);
      }
      resultSet.close();
      statement.close();
      return fileKey;
   }

   public int getNewFile() {
      String SELECT_KEY = "SELECT max(file_key)+1 FROM "+schema+".files";
      PreparedStatement statement = connection.prepareStatement(SELECT_KEY);
      ResultSet resultSet = statement.executeQuery();
      int fileKey = -1;
      while (resultSet.next()) {
        fileKey = resultSet.getInt(1);
      }
      resultSet.close();
      statement.close();
      return fileKey;
   }

   public void insertNewFile(int fileKey, int versionKey, int directoryKey,
                             String type, File tarFile) {
      String INSERT = "INSERT INTO "+schema+".files (file_key,version_key,directory_key,file_type,file_name,file_size,file_mod_time) VALUES (?,?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(INSERT);
      statement.setInt(1, fileKey);
      statement.setInt(2, versionKey);
      statement.setInt(3, directoryKey);
      statement.setString(4, type);
      statement.setString(5, tarFile.getName());
      statement.setLong(6, tarFile.length());
      statement.setTimestamp(7, new Timestamp(tarFile.lastModified()));
      statement.executeUpdate();
      statement.close();
   }

   public void updateExisting(int fileKey, File tarFile) {
      String UPDATE = "UPDATE "+schema+".files SET file_size=?,file_mod_time=? WHERE file_key=?";
      PreparedStatement statement = connection.prepareStatement(UPDATE);
      statement.setLong(1, tarFile.length());
      statement.setTimestamp(2, new Timestamp(tarFile.lastModified()));
      statement.setInt(3, fileKey);
      statement.executeUpdate();
      statement.close();
   }
}

