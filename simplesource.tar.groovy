/**
 * Packager used for gz and bz2 files where the name has the form:
 *    package-version.tar.(gz|bz2)
 * that is repackaged with an added suffix. So this would work for:
 *    vim-7.1.4.tar.gz
 * shipping as
 *    vim-7.1.4-src.tar.gz
 */
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
