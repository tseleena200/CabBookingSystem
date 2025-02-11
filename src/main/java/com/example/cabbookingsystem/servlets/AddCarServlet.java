package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.CarDAO;
import com.example.cabbookingsystem.model.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/addCar")
public class AddCarServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddCarServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        // Retrieve form data
        String model = request.getParameter("car_model");
        String licensePlate = request.getParameter("license_plate");
        String capacityStr = request.getParameter("capacity");
        String driverIdStr = request.getParameter("driver_id");

        LOGGER.log(Level.INFO, " Received Add Car request - Model: {0}, License Plate: {1}, Capacity: {2}, Driver ID: {3}",
                new Object[]{model, licensePlate, capacityStr, driverIdStr});

        //  Validate required fields
        if (model == null || licensePlate == null || capacityStr == null ||
                model.isEmpty() || licensePlate.isEmpty() || capacityStr.isEmpty()) {
            out.write("missing_fields");
            LOGGER.warning(" Missing required fields");
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            out.write("invalid_capacity");
            LOGGER.warning(" Invalid capacity format");
            return;
        }

        // Convert driver ID safely
        Integer driverId = null;
        if (driverIdStr != null && !driverIdStr.isEmpty()) {
            try {
                driverId = Integer.parseInt(driverIdStr);
            } catch (NumberFormatException e) {
                out.write("invalid_driver_id");
                LOGGER.warning(" Invalid driver ID format");
                return;
            }
        }

        CarDAO carDAO = CarDAO.getInstance();

        //  Check for duplicate license plate
        if (carDAO.isDuplicateLicense(licensePlate)) {
            out.write("duplicate_license");
            LOGGER.warning(" Duplicate license plate detected: " + licensePlate);
            return;
        }

        //  Check if the driver is already assigned to another car
        if (driverId != null && carDAO.isDriverAssigned(driverId)) {
            out.write("driver_already_assigned");
            LOGGER.warning(" Driver ID " + driverId + " is already assigned to another car");
            return;
        }

        //  Insert car into database
        Car newCar = new Car(model, licensePlate, capacity, driverId);
        boolean success = carDAO.addCar(newCar);

        if (success) {
            out.write("success");
            LOGGER.info(" Car added successfully!");
        } else {
            out.write("db_insert_fail");
            LOGGER.severe(" Failed to insert car into database");
        }
    }
}
