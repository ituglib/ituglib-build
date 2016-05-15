/**
 * Packager used for gz and bz2 files where the name has the form:
 *    package-version-something-release.tar.(gz|bz2)
 * that is repackaged with a different name replacing release with
 * a suffix. So this would work for:
 *    wget-1.17.1-b392-release.tar.gz
 * shipping as
 *    wget-1.17.1-b392-nse.tar.gz
 */
import hudson.model.*;
import java.sql.*;

ReleaseQualifiedPackageEngine engine = new ReleaseQualifiedPackageEngine();
JdbcLoader loader = new JdbcLoader();

String schema = System.getenv("SCHEMA");
engine.setSchema(schema);

Connection connection = loader.getConnection();
engine.setConnection(connection);

engine.execute();
connection.close();
