package com.example.cabbookingsystem.dao;

import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static UserDAO instance; // Singleton Instance
    private final Connection connection;

    private UserDAO() {
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            synchronized (UserDAO.class) {
                if (instance == null) {
                    instance = new UserDAO();
                }
            }
        }
        return instance;
    }

    // ✅ REGISTER USER - Now includes the role field
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (customer_registration_number, full_name, address, nic_number, email, password_hash, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getCustomerRegistrationNumber());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getAddress());
            stmt.setString(4, user.getNicNumber());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPassword());
            stmt.setString(7, user.getRole()); // ✅ Store user role

            return stmt.executeUpdate() > 0; // ✅ Return true if user is inserted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ GET USER BY EMAIL - Now retrieves role
    public User getUserByEmail(String email) {
        String query = "SELECT customer_registration_number, full_name, address, nic_number, email, password_hash, role FROM users WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("customer_registration_number"),
                        rs.getString("full_name"),
                        rs.getString("address"),
                        rs.getString("nic_number"),
                        rs.getString("email"),
                        rs.getString("password_hash"), // ✅ Retrieve hashed password
                        rs.getString("role") // ✅ Retrieve user role
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
