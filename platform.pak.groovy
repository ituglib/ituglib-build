/**
 * Packager used for pak files where the name has the form:
 *    package-version-release.pak.bin
 * that is repackaged with a different name replacing release with
 * a suffix. So this would work for:
 *    gmake-4.1g-release-nse.pak.bin
 * shipping as
 *    curl-4.1g-nse.pak.bin
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


PlatformPakPackageEngine engine = new PlatformPakPackageEngine();
JdbcLoader loader = new JdbcLoader();

String schema = System.getenv("SCHEMA");
engine.setSchema(schema);

Connection connection = loader.getConnection();
engine.setConnection(connection);

engine.execute();
connection.close();
