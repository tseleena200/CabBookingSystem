package com.example.cabbookingsystem.dao;

import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    //  REGISTER USER
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (customer_registration_number, full_name, address, nic_number, email, password_hash, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getCustomerRegistrationNumber());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getAddress());
            stmt.setString(4, user.getNicNumber());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPassword());
            stmt.setString(7, user.getRole());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    //  GET USER BY EMAIL
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
                        rs.getString("password_hash"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<User> getAllEmployees() {
        List<User> employees = new ArrayList<>();
        String query = "SELECT id, customer_registration_number, full_name, address, nic_number, email, role FROM users WHERE role = 'employee'";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employees.add(new User(
                        rs.getInt("id"),
                        rs.getString("customer_registration_number"),
                        rs.getString("full_name"),
                        rs.getString("address"),
                        rs.getString("nic_number"),
                        rs.getString("email"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("🚨 ERROR: Failed to fetch employees!");
        }

        return employees;
    }





    // Update employee details
    public boolean updateEmployee(String customerRegistrationNumber, String fullName, String address, String nicNumber, String email, String role) {
        String query = "UPDATE users SET full_name = ?, address = ?, nic_number = ?, email = ?, role = ? WHERE customer_registration_number = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fullName);
            statement.setString(2, address);
            statement.setString(3, nicNumber);
            statement.setString(4, email);
            statement.setString(5, role);
            statement.setString(6, customerRegistrationNumber);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getEmployeeByCustomerRegistrationNumber(String customerRegistrationNumber) {
        String query = "SELECT id, customer_registration_number, full_name, address, nic_number, email, role FROM users WHERE customer_registration_number = ? AND role = 'employee'";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customerRegistrationNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("customer_registration_number"),
                        rs.getString("full_name"),
                        rs.getString("address"),
                        rs.getString("nic_number"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if employee not found
    }


    // DELETE EMPLOYEE
    public boolean deleteEmployee(String customerRegistrationNumber) {
        String query = "DELETE FROM users WHERE customer_registration_number = ? AND role = 'employee'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customerRegistrationNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }





    // Check if email already exists
    public boolean isDuplicateEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0; // Returns true if email exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if NIC already exists
    public boolean isDuplicateNIC(String nic) {
        String sql = "SELECT COUNT(*) FROM users WHERE nic_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nic);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0; // Returns true if NIC exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if customer registration number already exists
    public boolean isDuplicateCustomerRegNum(String customerRegNum) {
        String sql = "SELECT COUNT(*) FROM users WHERE customer_registration_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customerRegNum);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0; // Returns true if registration number exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
