/*
 * This file connects to the database server on AMAZON.
 * Author - OARLOK Team
 * Data Modified - 7th Aug 2012
 * 
 */
package oarlok.resources.dbo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	private String jdbcUsername = "flippers";
	private String jdbcPassword = "framehawk";
	private String jdbcURL = "jdbc:mysql://row.c7irj4yygfgf.us-west-1.rds.amazonaws.com:3306/row";	
	Connection myConnection = null;
	private String getJdbcUsername() {
		return jdbcUsername;
	}
	private String getJdbcPassword() {
		return jdbcPassword;
	}
	private String getJdbcURL() {
		return jdbcURL;
	}
	public Connection getConnectionHandler() {
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver).newInstance();
			myConnection = DriverManager.getConnection(getJdbcURL(), getJdbcUsername(),
					getJdbcPassword());
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return myConnection;
	}
}
