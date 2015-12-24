/**
 * Packager used for gz and bz2 files where the name has the form:
 *    package-version-something.tar.(gz|bz2)
 * that is repackaged with the same name. So this would work for:
 *    curl-7.46.0-src.tar.gz
 */
import hudson.model.*;
import java.sql.*;

NormalPackageEngine engine = new NormalPackageEngine();
JdbcLoader loader = new JdbcLoader();

String schema = System.getenv("SCHEMA");
engine.setSchema(schema);

Connection connection = loader.getConnection();
engine.setConnection(connection);

engine.execute();
connection.close();
