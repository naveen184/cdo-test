package com.test.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 ****************************************************************************
 * HIGHLIGHTS: > Base methods to perform a query to the data base. Step 1 - Open
 * Connection Step 2 - Execute the query Step 3 - Close the connection Data:
 * Information needed is: host, port, service name, username, password and
 * query. This data can be set in a data class that keeps different values using
 * switch
 ****************************************************************************
 */

/**
 * @deprecated  reason this method is deprecated <br/>
 *              {will be removed in next version} <br/>
 *              Use the {@link BaseQuery} instead of this one.
 */
public class DBConnection {

	 private DBConnection() {
		    throw new IllegalStateException("Utility class");
		  }
	
    /**
     * OBJECTIVE: Step 1 to perform a query on DB - Open Connection.
     * 
     * @throws SQLException
     */
    public static Connection openConnection(String host, String port, String serviceName, String username,
	    String password) throws SQLException {

	return DriverManager.getConnection(
		"jdbc:oracle:thin:" + username + "/" + password + "@" + host + ":" + port + "/" + serviceName);
    }

    /**
     * OBJECTIVE: Step 2 execute the query statement in the DB.
     */
    public static ResultSet executeQuery(Connection con, String queryStmt) throws SQLException {
    	Statement stmt = con.createStatement();
    	try {
    		
    		return stmt.executeQuery(queryStmt);
		} finally {
			if(stmt != null){
				stmt.close();
			}
			con.close();
		}
    }

    /**
     * OBJECTIVE: Step 3 After the Result Set is used then close connection.
     */
    public static void closeConnection(Connection con) throws SQLException {
	con.close();
    }

}
