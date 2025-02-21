package com.example.cabbookingsystem.service;

import com.example.cabbookingsystem.dao.CarDAO;
import com.example.cabbookingsystem.model.Car;

public class CarService {
    private final CarDAO carDAO = CarDAO.getInstance();

    // Method to delete a car by its ID
    public String deleteCar(int carId) {
        boolean deleted = carDAO.deleteCar(carId);
        return deleted ? "success" : "delete_failed";
    }
    // Add a car and handle necessary validations
    public String addCar(String model, String licensePlate, int capacity, Integer driverId) {
        // Check if the car's license plate is a duplicate
        if (carDAO.isDuplicateLicense(licensePlate)) {
            return "duplicate_license";
        }

        // Check if the driver is already assigned to another car
        if (driverId != null && carDAO.isDriverAssigned(driverId)) {
            return "driver_already_assigned";
        }

        // Create a new car and insert it into the database
        Car newCar = new Car(model, licensePlate, capacity, driverId);
        boolean success = carDAO.addCar(newCar);
        return success ? "success" : "db_insert_fail";
    }

    // Update car details
    public String updateCar(int carId, String model, String licensePlate, int capacity, Integer driverId) {
        // Check if car exists, and update it
        Car car = new Car(model, licensePlate, capacity, driverId);
        car.setId(carId);

        boolean updated = carDAO.updateCar(car);
        return updated ? "success" : "update_failed";
    }
}
