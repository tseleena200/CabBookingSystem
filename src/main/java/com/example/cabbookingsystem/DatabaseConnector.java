package com.example.cabbookingsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/MegaCityCab_DB";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    private static Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseConnector() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure MySQL JDBC Driver is loaded
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // 🔹 TEST CONNECTION
    public static void main(String[] args) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn != null) {
            System.out.println("🎉 Connection established successfully.");
        } else {
            System.out.println("❌ Connection failed.");
        }
    }
}
