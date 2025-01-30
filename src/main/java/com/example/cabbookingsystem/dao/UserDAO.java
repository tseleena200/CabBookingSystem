package com.example.cabbookingsystem.dao;

import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Method to register a new user
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (customer_registration_number, full_name, address, nic_number, email, password_hash) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn == null) {
                System.err.println("❌ Database connection is null! Check DatabaseConnector.");
                return false;
            }

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getCustomerRegistrationNumber());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getAddress());
            stmt.setString(4, user.getNicNumber());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPassword());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Return true if user is inserted successfully

        } catch (SQLException e) {
            System.err.println("❌ Error inserting user into database.");
            e.printStackTrace();
        }
        return false;
    }

    // Method to check if a user exists (Login Verification)
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";

        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn == null) {
                System.err.println("❌ Database connection is null! Check DatabaseConnector.");
                return null;
            }

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("customer_registration_number"),
                        rs.getString("full_name"),
                        rs.getString("address"),
                        rs.getString("nic_number"),
                        rs.getString("email"),
                        rs.getString("password_hash")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving user from database.");
            e.printStackTrace();
        }
        return null; // No user found
    }
}
