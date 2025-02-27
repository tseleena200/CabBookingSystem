package com.example.cabbookingsystem.service;

import com.example.cabbookingsystem.dao.CarDAO;
import com.example.cabbookingsystem.dao.DriverDAO;
import com.example.cabbookingsystem.model.Car;
import com.example.cabbookingsystem.model.Driver;

public class DriverService {

    private final DriverDAO driverDAO = DriverDAO.getInstance();
    private final CarDAO carDAO = CarDAO.getInstance();

    public String addDriver(String fullName, String licenseNumber, String contactNumber) {
        // Check if the license number is already used
        if (driverDAO.isDuplicateLicense(licenseNumber)) {
            return "duplicate_license";  // License already exists
        }

        // Create new driver object and insert into database
        Driver newDriver = new Driver(fullName, licenseNumber, contactNumber);
        boolean success = driverDAO.addDriver(newDriver);

        return success ? "success" : "db_insert_fail";
    }

    public String deleteDriver(int driverId) {
        // First, check if the driver has an assigned car
        Car car = carDAO.getCarByDriverId(driverId);

        // If the driver has an assigned car, delete the car
        if (car != null) {
            boolean carDeleted = carDAO.deleteCar(car.getId()); // Delete the car
            if (!carDeleted) {
                return "delete_car_failed";
            }
        }
        //  delete the driver
        boolean deleted = driverDAO.deleteDriver(driverId);
        return deleted ? "success" : "delete_failed";
    }
    // Method to update driver
    public String updateDriver(int driverId, String fullName, String licenseNumber, String contactNumber) {
        // Create updated driver object
        Driver driver = new Driver(driverId, fullName, licenseNumber, contactNumber);

        // Call DAO to update the driver in the database
        boolean updated = driverDAO.updateDriver(driver);

        if (updated) {
            return "success";
        } else {
            return "update_failed";
        }
    }

}
