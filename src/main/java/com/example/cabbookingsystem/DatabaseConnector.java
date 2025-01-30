package com.example.cabbookingsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton Pattern for Database Connection
public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/MegaCityCab_DB";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    private static Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseConnector() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load MySQL JDBC Driver (optional for newer versions)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish database connection
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connected successfully!");

            } catch (ClassNotFoundException e) {
                System.err.println("❌ MySQL Driver Not Found!");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("❌ Database Connection Failed! Check credentials & database.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    // 🔹 TEST CONNECTION
    public static void main(String[] args) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn != null) {
            System.out.println("✅ Connection established successfully.");
        } else {
            System.out.println("❌ Connection failed.");
        }
    }
}
