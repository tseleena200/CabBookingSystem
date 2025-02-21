package com.example.cabbookingsystem.service;

import com.example.cabbookingsystem.dao.DriverDAO;
import com.example.cabbookingsystem.model.Driver;

public class DriverService {

    private final DriverDAO driverDAO = DriverDAO.getInstance();

    public String addDriver(String fullName, String licenseNumber, String contactNumber) {
        // Check if the license number is already used
        if (driverDAO.isDuplicateLicense(licenseNumber)) {
            return "duplicate_license";  // License already exists
        }

        // Create new driver object and insert into database
        Driver newDriver = new Driver(fullName, licenseNumber, contactNumber);
        boolean success = driverDAO.addDriver(newDriver);

        return success ? "success" : "db_insert_fail";  // Return success or failure
    }

    public String deleteDriver(int driverId) {
        boolean deleted = driverDAO.deleteDriver(driverId);
        if (deleted) {
            return "success";
        } else {
            return "delete_failed";  // Return an error message if deletion fails
        }
    }
    // Method to update driver
    public String updateDriver(int driverId, String fullName, String licenseNumber, String contactNumber) {
        // Create updated driver object
        Driver driver = new Driver(driverId, fullName, licenseNumber, contactNumber);

        // Call DAO to update the driver in the database
        boolean updated = driverDAO.updateDriver(driver);

        if (updated) {
            return "success";  // Return success if the update was successful
        } else {
            return "update_failed";  // Return error message if the update failed
        }
    }
}
