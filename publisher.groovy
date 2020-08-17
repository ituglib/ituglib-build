/**
 * Packager used for gz and bz2 files where the name has the form:
 *    package-version-sub-something.tar.(gz|bz2)
 * that is repackaged with the same name. So this would work for:
 *    sed-7.46.0-318f-src.tar.gz
 */
import hudson.model.*;
import java.sql.*;
@Library('ituglib-stage') _

def publishSource() {
	NormalQualifiedPackageEngine engine = new NormalQualifiedPackageEngine();
	JdbcLoader loader = new JdbcLoader();

	String schema = System.getenv("SCHEMA");
	engine.setSchema(schema);

	Connection connection = loader.getConnection();
	engine.setConnection(connection);

	engine.execute();
	connection.close();
}

return this;
