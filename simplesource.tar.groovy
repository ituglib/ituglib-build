import hudson.model.*;
import java.sql.*;

BareSourcePackageEngine engine = new BareSourcePackageEngine();
JdbcLoader loader = new JdbcLoader();

String schema = System.getenv("SCHEMA");
engine.setSchema(schema);

Connection connection = loader.getConnection();
engine.setConnection(connection);

engine.execute();
connection.close();
