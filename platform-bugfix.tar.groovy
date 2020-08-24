/**
 * Packager used for gz and bz2 files where the name has the form:
 *    package-version-release.tar.(gz|bz2)
 * that is repackaged with a different name replacing release with
 * a suffix. So this would work for:
 *    rsync-7.46.0-2-gff00123-release-nse.tar.gz
 * shipping as
 *    rsync-7.46.0-2-gff00123-nse.tar.gz
 */
import hudson.model.*;
import java.sql.*;

boolean debug = System.getenv("DEBUG") != null;
if (debug) {
	println "Platform Groovy Script";
}
if (System.getenv("BASENAME") == null) {
	println "Missing BASENAME";
	System.exit(1);
}
	
if (System.getenv("DIST") == null) {
	println "Missing DIST";
	System.exit(1);
}
if (System.getenv("DEST") == null) {
	println "Missing DEST";
	System.exit(1);
}
if (System.getenv("TYPE") == null) {
	println "Missing TYPE";
	System.exit(1);
}


PlatformBugfixPackageEngine engine = new PlatformBugFixPackageEngine();
JdbcLoader loader = new JdbcLoader();

String schema = System.getenv("SCHEMA");
engine.setSchema(schema);

Connection connection = loader.getConnection();
engine.setConnection(connection);

engine.execute();
connection.close();
