package org.example.readnigstore.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    public static final String URL = "jdbc:mysql://localhost:3306/reading_store";
    public static final String USER = "root";
    public static final String PASSWORD = "anshu123";


    //connetion to the database
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection to the database established successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to establish connection: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
        return connection;
    }

    //close the connection
    public static void closeConnection(Connection connection) {
        try {
            connection.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to close connection: " + e.getMessage());
        }
    }
}
