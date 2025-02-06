package com.example.cabbookingsystem.dao;

import com.example.cabbookingsystem.model.Driver;
import com.example.cabbookingsystem.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {
    private static DriverDAO instance;
    private final Connection connection;

    private DriverDAO() {
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    public static DriverDAO getInstance() {
        if (instance == null) {
            synchronized (DriverDAO.class) {
                if (instance == null) {
                    instance = new DriverDAO();
                }
            }
        }
        return instance;
    }

    //  Check if the license number already exists
    public boolean isDuplicateLicense(String licenseNumber) {
        String sql = "SELECT COUNT(*) FROM drivers WHERE license_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, licenseNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Add a new driver
    public boolean addDriver(Driver driver) {
        if (isDuplicateLicense(driver.getLicenseNumber())) {
            return false; // Prevent duplicate inserts
        }

        String sql = "INSERT INTO drivers (full_name, license_number, contact_number) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, driver.getFullName());
            stmt.setString(2, driver.getLicenseNumber());
            stmt.setString(3, driver.getContactNumber());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all drivers (to populate dropdown in add_car.jsp)
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String query = "SELECT id, full_name FROM drivers";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Driver driver = new Driver();
                driver.setId(rs.getInt("id"));
                driver.setFullName(rs.getString("full_name"));
                drivers.add(driver);
            }

        } catch (SQLException e) {
            System.out.println("🚨 [ERROR] Failed to fetch drivers: " + e.getMessage());
            e.printStackTrace();
        }
        return drivers;
    }
}
