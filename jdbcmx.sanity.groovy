import hudson.model.*;
import java.sql.*;

boolean debug = System.getenv("DEBUG") != null;
if (debug) {
	println "JDBC Sanity Check"
}

println "CLASSPATH="+System.getenv("CLASSPATH");
println "JAVA_HOME="+System.getenv("JAVA_HOME");
println "_RLD_LIB_PATH="+System.getenv("_RLD_LIB_PATH");
println "PATH="+System.getenv("PATH");

JdbcLoader loader = new JdbcLoader();

Connection connection = loader.getConnection();

connection.close();
