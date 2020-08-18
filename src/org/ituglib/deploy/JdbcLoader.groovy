#!/usr/bin/env groovy

package org.ituglib.deploy;

import hudson.model.*;
import java.io.*;
import java.sql.*;
import java.util.regex.*;

class JdbcLoader {

	static private final String JDBC_MX_TAR =
		"/usr/tandem/jdbcMx/current/lib/jdbcMx.jar";
	static private final String JDBC_DRIVER = 
		"com.tandem.sqlmx.SQLMXDriver";

	public JdbcLoader() {
	}

	public Connection getConnection() {
		File localFile = new File(JDBC_MX_TAR);
		if (localFile == null) {
			echo "Could not load File object";
			return null;
		} else if (!localFile.exists()) {
			echo JDBC_MX_TAR+" does not exist";
			return null;
		}
		// Use the groovy script's classLoader to add the jar file at
		// runtime.
		String uri = localFile.toURI().toString();
		echo "My uri is ${uri}";
		String url = localFile.toURI().toURL().toString();
		echo "My url is ${url}";
		this.class.classLoader.rootLoader.addURL(
			localFile.toURI().toURL());

		// Now register the driver.
		Class.forName(JDBC_DRIVER);

		Connection connection =
			DriverManager.getConnection("jdbc:sqlmx:");
		connection.setAutoCommit(false);
		return connection;
	}
}

