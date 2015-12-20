import hudson.model.*;
import java.io.*;
import java.sql.*;

String schema = System.getenv("SCHEMA");
String basename = System.getenv("BASENAME");
String destination = System.getenv("DEST");
String prefix = System.getenv("PREFIX");
String suffix = System.getenv("SUFFIX");
String type = System.getenv("TYPE");
String packageName = prefix == null ? basename : prefix + basename;
String workspace = System.getenv("WORKSPACE");
String versionShell = String.format('sh %s/../../Ituglib_Build/workspace/%s.version',
                                    workspace, basename);
String version = versionShell.execute().text;
String url = System.getenv("URL");
String targetShell = String.format('sh %s/../../Ituglib_Build/workspace/package.stage.target',
                                    workspace);
File destinationDirectory = new File(destination);
String archive = packageName.trim()+'-'+version.trim()+suffix.trim()+'.tar.gz';
File tarFile = new File(destinationDirectory, archive);
if (! tarFile.exists()) {
   println tarFile.getAbsolutePath() + ' does not exist.';
   System.exit(1);
}
println "Preparing distribution: "+tarFile.getAbsolutePath();

File localFile = new File("/usr/tandem/jdbcMx/current/lib/jdbcMx.jar");
// Use the groovy script's classLoader to add the jar file at runtime.
this.class.classLoader.rootLoader.addURL(localFile.toURI().toURL());

// Now register the driver.
Class.forName("com.tandem.sqlmx.SQLMXDriver");

Connection connection = DriverManager.getConnection("jdbc:sqlmx:");
connection.setAutoCommit(false);

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

int directoryKey = new Directories(schema, connection).getKey(destination);
if (directoryKey < 0) {
   println "Cannot find directory "+destination+"\n";
   System.exit(1);
}
println "Found "+destination;

int packageKey = new Packages(schema, connection).getKey(packageName);
if (packageKey < 0) {
   println "Cannot find package "+packageName;
   System.exit(1);
}
println "Found "+packageName;

Versions versions = new Versions(schema, connection);
int versionKey = versions.getKey(packageKey, version);
if (versionKey < 0) {
   versionKey = versions.getNewVersion();
   if (versionKey < 0) {
      versionKey = 1;
      println "Initial version key";
   } else {
      println "New version detected for "+version;
   }
   versions.insertNewVersion(versionKey, packageName, packageKey, version, url);
   println "New version entry created for "+version;
} else {
   println "Found version "+version;
}

FileSet fileset = new FileSet(schema, connection);
int fileKey = fileset.getKey(versionKey, directoryKey, type, tarFile);
if (fileKey < 0) {
   fileKey = fileset.getNewFile();
   if (fileKey < 0) {
      fileKey = 1;
      println "Initial file key"
   } else {
      println "New file "+tarFile.getName();
   }
   fileset.insertNewFile(fileKey, versionKey, directoryKey, type, tarFile);
} else {
   println "Found "+tarFile.getName();
   fileset.updateExisting(fileKey, tarFile);
}
connection.commit();
connection.close();
