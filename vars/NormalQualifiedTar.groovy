#!/usr/bin/env groovy

/**
 * Normal Qualified Packager script for direct use from a Pipeline.
 *
 * Packager used for gz and bz2 files where the name has the form:
 *    package-version-sub-something.tar.(gz|bz2)
 * that is repackaged with the same name. So this would work for:
 *    sed-7.46.0-318f-src.tar.gz
 */
import hudson.model.*;
import java.sql.*;
import org.ituglib.deploy.*;

def call(String packageName = 'unknown', String prefix = '',
	String suffix = '', String nonstopExtensions = null,
	String dependencies = null, String readmeFile = null) {
        echo "Hello, ${packageName} and " + GlobalVars.foo;

	try {
	NormalQualifiedPackageEngine engine =
		new NormalQualifiedPackageEngine();
	echo "Returned from constructor";
	engine.updateBasename(packageName);
	engine.updatePrefix(prefix);
	engine.updateSuffix(suffix);
	engine.updateNonstopExtensions(nonstopExtensions);
	engine.updateDependencies(dependencies);
	engine.updateReadmeFile(readmeFile);
	//JdbcLoader loader = new JdbcLoader();

	//String schema = System.getenv("SCHEMA");
	//engine.updateSchema(schema);

	//Connection connection = loader.getConnection();
	//engine.set(connection);

	//engine.execute();
	//connection.close();
	} catch (Throwable t) {
		t.printStackTrace();
	}
}

