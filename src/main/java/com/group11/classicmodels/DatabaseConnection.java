package com.group11.classicmodels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Christian Douglas Farnes Fancy
 */

public class DatabaseConnection {
    // Define constants for the database URL, username, and password
	 private static final String DB_URL = "jdbc:mysql://localhost/classicmodels";
	 private static final String DB_USER = "student";
	 private static final String DB_PASSWORD = "student";

    // Declare a Connection object to manage the database connection
    private Connection connection;

    // Constructor for initializing the database connection
    public DatabaseConnection() {
        try {
            // Load the MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish a connection to the database using the provided URL, username, and password
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            // Handle any exceptions that may occur during connection setup
            e.printStackTrace();
        }
    }

    // Getter method to retrieve the database connection
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    // Method to close the database connection
    public void closeConnection() {
        try {
            // Check if the connection is not null before closing it
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // Handle any exceptions that may occur while closing the connection
            e.printStackTrace();
        }
    }

	public static void setConnection(Connection testConnection) {
		// TODO Auto-generated method stub
		
	}
}
