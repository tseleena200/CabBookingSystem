package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/calculateBill")
public class CalculateBillServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CalculateBillServlet.class.getName());

    // Create an instance of the BookingService
    private final BookingService bookingService = new BookingService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        // Retrieve the order number from the request
        String orderNumber = request.getParameter("order_number");
        if (orderNumber != null) {
            orderNumber = orderNumber.trim();
        }

        LOGGER.log(Level.INFO, "Received order number: {0}", orderNumber);

        // Check if the order number is provided
        if (orderNumber == null || orderNumber.isEmpty()) {
            response.getWriter().write("missing_order_number");
            LOGGER.log(Level.WARNING, "Missing order number.");
            return;
        }

        // Retrieve the distance, base fare, tax rate, and discount rate from the request
        String distanceStr = request.getParameter("distance");
        String baseFareStr = request.getParameter("base_fare");
        String taxRateStr = request.getParameter("tax_rate");
        String discountRateStr = request.getParameter("discount_rate");

        // Check if the required parameters are provided
        if (distanceStr == null || baseFareStr == null || distanceStr.isEmpty() || baseFareStr.isEmpty()) {
            response.getWriter().write("missing_values");
            LOGGER.log(Level.WARNING, "Missing distance or base fare values.");
            return;
        }

        double distance, baseFare, taxRate = 0, discountRate = 0;
        try {
            // Parse the input parameters
            distance = Double.parseDouble(distanceStr);
            baseFare = Double.parseDouble(baseFareStr);
            if (taxRateStr != null && !taxRateStr.isEmpty()) {
                taxRate = Double.parseDouble(taxRateStr);
            }
            if (discountRateStr != null && !discountRateStr.isEmpty()) {
                discountRate = Double.parseDouble(discountRateStr);
            }
        } catch (NumberFormatException e) {
            response.getWriter().write("invalid_numbers");
            LOGGER.log(Level.WARNING, "Invalid number format in input.");
            return;
        }

        LOGGER.log(Level.INFO, "Received Distance: {0}, Base Fare: {1}, Tax: {2}%, Discount: {3}% for Order: {4}",
                new Object[]{distance, baseFare, taxRate, discountRate, orderNumber});

        // Call the BookingService to calculate the bill and update the booking
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);

        if (result.startsWith("success:")) {
            // Return the calculated bill as part of the response
            response.getWriter().write(result);
            LOGGER.log(Level.INFO, "Bill updated successfully for Order Number: {0}", orderNumber);
        } else {
            // Return an error message
            response.getWriter().write(result);
            LOGGER.log(Level.SEVERE, "Error calculating bill for Order Number: {0}", orderNumber);
        }
    }
}
