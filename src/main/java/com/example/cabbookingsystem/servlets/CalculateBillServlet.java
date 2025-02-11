package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.BookingDAO;
import com.example.cabbookingsystem.model.Booking;
import com.example.cabbookingsystem.util.EmailSender;
import jakarta.mail.MessagingException;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        //  Retrieve Order Number
        String orderNumber = request.getParameter("order_number");
        if (orderNumber != null) {
            orderNumber = orderNumber.trim();
        }

        LOGGER.log(Level.INFO, " Received order number: {0}", orderNumber);

        if (orderNumber == null || orderNumber.isEmpty()) {
            response.getWriter().write("missing_order_number");
            LOGGER.log(Level.WARNING, " Missing order number.");
            return;
        }

        // Fetch Booking Details
        BookingDAO bookingDAO = BookingDAO.getInstance();
        Booking booking = bookingDAO.getBookingByOrderNumber(orderNumber);
        if (booking == null) {
            response.getWriter().write("booking_not_found");
            LOGGER.log(Level.WARNING, " Booking not found for order number: {0}", orderNumber);
            return;
        }
        // Check if bill has already been calculated
        if (bookingDAO.isBillAlreadyCalculated(orderNumber)) {
            response.getWriter().write("already_calculated");
            return;
        }
        //  Ensure booking is confirmed before calculating the bill
        LOGGER.log(Level.INFO, " Booking Status for Order {0}: {1}", new Object[]{orderNumber, booking.getStatus()});
        if (!"Confirmed".equals(booking.getStatus())) {
            response.getWriter().write("not_confirmed");
            LOGGER.log(Level.WARNING, " Booking Order {0} is not confirmed yet.", orderNumber);
            return;
        }

        //  Validate Distance, Base Fare, Tax Rate, and Discount Rate
        String distanceStr = request.getParameter("distance");
        String baseFareStr = request.getParameter("base_fare");
        String taxRateStr = request.getParameter("tax_rate");
        String discountRateStr = request.getParameter("discount_rate");

        if (distanceStr == null || baseFareStr == null || distanceStr.isEmpty() || baseFareStr.isEmpty()) {
            response.getWriter().write("missing_values");
            LOGGER.log(Level.WARNING, " Missing distance or base fare values.");
            return;
        }

        double distance, baseFare, taxRate = 0, discountRate = 0;
        try {
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
            LOGGER.log(Level.WARNING, "⚠ Invalid number format in input.");
            return;
        }

        LOGGER.log(Level.INFO, "Received Distance: {0}, Base Fare: {1}, Tax: {2}%, Discount: {3}% for Order: {4}",
                new Object[]{distance, baseFare, taxRate, discountRate, orderNumber});

        //   Initial Fare
        double totalFare = booking.calculateFare(baseFare, distance,taxRate, discountRate);

        //  Apply Tax and Discount
        double taxAmount = (totalFare * taxRate) / 100;
        double discountAmount = (totalFare * discountRate) / 100;
        totalFare = totalFare + taxAmount - discountAmount;

        LOGGER.log(Level.INFO, " Calculated Total Fare after Tax & Discount: {0} (Tax: {1}, Discount: {2}) for Order: {3}",
                new Object[]{totalFare, taxAmount, discountAmount, orderNumber});

        //  Update Booking Object
        booking.setBaseFare(baseFare);
        booking.setDistance(distance);
        booking.setTotalAmount(totalFare);

        //  Update Booking with Final Amount in DB
        boolean updated = bookingDAO.updateBookingDetails(orderNumber, baseFare, distance, taxRate, discountRate, totalFare);
        if (updated) {
            response.getWriter().write("success:" + String.format("%.2f", totalFare));
            LOGGER.log(Level.INFO, " Bill updated successfully! Final Total Fare: {0} for Order Number: {1}",
                    new Object[]{totalFare, orderNumber});

            //  Send Bill Email after successful update
            try {
                EmailSender.sendBillEmail(booking);
                LOGGER.log(Level.INFO, " Email sent successfully to: {0}", booking.getCustomerEmail());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, " Failed to send bill email to: {0}", booking.getCustomerEmail());
            }
        }}}