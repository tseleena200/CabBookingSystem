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

@WebServlet("/addCar")
public class AddCarServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        // Retrieve form data
        String model = request.getParameter("car_model");
        String licensePlate = request.getParameter("license_plate");
        String capacityStr = request.getParameter("capacity");
        String driverIdStr = request.getParameter("driver_id");

        //  Validate fields
        if (model == null || licensePlate == null || capacityStr == null || model.isEmpty() || licensePlate.isEmpty() || capacityStr.isEmpty()) {
            out.write("missing_fields");
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            out.write("invalid_capacity");
            return;
        }

        //  Convert driver ID safely
        Integer driverId = null;
        if (driverIdStr != null && !driverIdStr.isEmpty()) {
            try {
                driverId = Integer.parseInt(driverIdStr);
            } catch (NumberFormatException e) {
                out.write("invalid_driver_id");
                return;
            }
        }

        //  Insert car into database
        Car newCar = new Car(model, licensePlate, capacity, driverId);
        boolean success = CarDAO.getInstance().addCar(newCar);

        if (success) {
            out.write("success");
        } else {
            out.write("db_insert_fail");
        }
    }
}
