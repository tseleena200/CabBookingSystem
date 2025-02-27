package com.example.cabbookingsystem;

import com.example.cabbookingsystem.service.CarService;
import com.example.cabbookingsystem.service.DriverService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DriverCarAvailabilityManagementTest {

    private DriverService driverService = new DriverService();
    private CarService carService = new CarService();

    @Test
    public void testAddDriverSuccessfully() {
        // Test Case ID: TC_001
        // Test Case Name: Add Driver Successfully
        // Test Case Objective: To check if the system allows an admin to add a driver successfully
        // Test Data
        String fullName = "Nelson";
        String licenseNumber = "AAC-12444";
        String contactNumber = "0506849584";

        // Expected Result
        String expectedResult = "success";

        // Call the addDriver method from DriverService
        String result = driverService.addDriver(fullName, licenseNumber, contactNumber);

        assertEquals(expectedResult, result,
                "Expected result: " + expectedResult + " but got: " + result);

    }
    @Test
    public void testAddCarSuccessfully() {
        // Test Case ID: TC_002
        // Test Case Name: Add Car Successfully
        // Test Case Objective: To check if the system allows an admin to add a car successfully
        // Test Data
        String carModel = "Honda Civic";
        String numberPlate = "YYY-5678";
        int capacity = 4;
        int driverId = 6;

        String expectedResult = "success";

        // Call the addCar method from CarService
        String result = carService.addCar(carModel, numberPlate, capacity, driverId);

        // Assert the result matches the expected outcome
        assertEquals(expectedResult, result,
                "Expected result: " + expectedResult + " but got: " + result);
    }

    @Test
    public void testEditDriverSuccessfully() {
        // Test Case ID: TC_003
        // Test Case Name: Edit Driver Successfully
        // Test Case Objective: To check if the system allows an admin to edit a driver's details successfully

        // Test Data
        int driverId = 6; // Existing driver ID
        String newFullName = "Rosie"; // New name to update
        String newLicenseNumber = "BCCCC-12244"; // New license number
        String newContactNumber = "0506849584";

        // Expected Result
        String expectedResult = "success";

        // Call the updateDriver method from DriverService
        String result = driverService.updateDriver(driverId, newFullName, newLicenseNumber, newContactNumber);
        assertEquals(expectedResult, result,
                "Expected result: " + expectedResult + " but got: " + result);
    }

    @Test
    public void testEditCarSuccessfully() {
        // Test Case ID: TC_004
        // Test Case Name: Edit Car Successfully
        // Test Case Objective: To check if the system allows an admin to edit a car's details successfully
        // Test Data
        int driverId = 3;
        int carId = 3; // Existing car ID
        String newCarModel = "Lamborghini"; // New car model to update
        String newLicensePlate = "LMNO789"; // New license plate
        int newCapacity = 4; // New capacity

        // Expected Result
        String expectedResult = "success";

        // Call the updateCar method from CarService
        String result = carService.updateCar(carId, newCarModel, newLicensePlate, newCapacity, driverId);

        // Assert the result matches the expected outcome
        assertEquals(expectedResult, result,
                "Expected result: " + expectedResult + " but got: " + result);
    }
}
