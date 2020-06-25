#!groovy

/**
 * Packager used for gz and bz2 files where the name has the form:
 *    package-version-release.tar.(gz|bz2)
 * that is repackaged with a different name replacing release with
 * a suffix. So this would work for:
 *    curl-7.46.0-release.tar.gz
 * shipping as
 *    curl-7.46.0-nse.tar.gz
 */
import hudson.model.*;
import java.sql.*;

def packageRelease() {
	ReleasePackageEngine engine = new ReleasePackageEngine();
	JdbcLoader loader = new JdbcLoader();

	String schema = System.getenv("SCHEMA");
	engine.setSchema(schema);

	Connection connection = loader.getConnection();
	engine.setConnection(connection);

	engine.execute();
	connection.close();
}

return [
	packageRelease: this.&packageRelease
]
