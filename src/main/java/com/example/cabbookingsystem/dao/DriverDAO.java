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

    // ✅ Check if the license number already exists
    public boolean isDuplicateLicense(String licenseNumber) {
        String sql = "SELECT COUNT(*) FROM drivers WHERE license_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, licenseNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("🚨 [ERROR] Failed to check duplicate license: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Add a new driver
    public boolean addDriver(Driver driver) {
        if (isDuplicateLicense(driver.getLicenseNumber())) {
            System.err.println("🚨 [ERROR] Duplicate license detected: " + driver.getLicenseNumber());
            return false; // Prevent duplicate inserts
        }

        String sql = "INSERT INTO drivers (full_name, license_number, contact_number) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, driver.getFullName());
            stmt.setString(2, driver.getLicenseNumber());
            stmt.setString(3, driver.getContactNumber());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    driver.setId(generatedKeys.getInt(1)); // ✅ Set generated driver ID
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("🚨 [ERROR] Failed to add driver: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Fetch all drivers
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String query = "SELECT id, full_name, license_number, contact_number FROM drivers";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Driver driver = new Driver();
                driver.setId(rs.getInt("id"));
                driver.setFullName(rs.getString("full_name"));
                driver.setLicenseNumber(rs.getString("license_number"));
                driver.setContactNumber(rs.getString("contact_number"));
                drivers.add(driver);
            }

        } catch (SQLException e) {
            System.err.println("🚨 [ERROR] Failed to fetch drivers: " + e.getMessage());
            e.printStackTrace();
        }
        return drivers;
    }

    // ✅ Fetch a specific driver by ID
    public Driver getDriverById(int driverId) {
        String query = "SELECT id, full_name, license_number, contact_number FROM drivers WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Driver driver = new Driver();
                driver.setId(rs.getInt("id"));
                driver.setFullName(rs.getString("full_name"));
                driver.setLicenseNumber(rs.getString("license_number"));
                driver.setContactNumber(rs.getString("contact_number"));
                return driver;
            }
        } catch (SQLException e) {
            System.err.println("🚨 [ERROR] Failed to fetch driver: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Update driver details
    public boolean updateDriver(Driver driver) {
        String query = "UPDATE drivers SET full_name = ?, license_number = ?, contact_number = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, driver.getFullName());
            stmt.setString(2, driver.getLicenseNumber());
            stmt.setString(3, driver.getContactNumber());
            stmt.setInt(4, driver.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("🚨 [ERROR] Failed to update driver: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Delete a driver by ID
    public boolean deleteDriver(int driverId) {
        String query = "DELETE FROM drivers WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("🚨 [ERROR] Failed to delete driver: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
