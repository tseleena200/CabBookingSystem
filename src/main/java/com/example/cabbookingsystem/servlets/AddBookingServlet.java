package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.service.BookingService;
import com.example.cabbookingsystem.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/addBooking")
public class AddBookingServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddBookingServlet.class.getName());
    private final BookingService bookingService = new BookingService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        // Check if the user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            out.write("unauthorized");
            LOGGER.log(Level.WARNING, "Unauthorized access attempt.");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String customerRegNumber = currentUser.getCustomerRegistrationNumber();

        // Log received parameters
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            LOGGER.log(Level.INFO, "Received Parameter: {0} = {1}", new Object[]{paramName, request.getParameter(paramName)});
        }

        // Retrieve form data
        String customerName = request.getParameter("customer_name");
        String customerAddress = request.getParameter("customer_address");
        String phoneNumber = request.getParameter("phone_number");
        String destination = request.getParameter("destination");
        String carIdStr = request.getParameter("car_id");
        String scheduledDateStr = request.getParameter("scheduled_date");
        String scheduledTimeStr = request.getParameter("scheduled_time");
        String fareType = request.getParameter("fare_type");
        String taxRateStr = request.getParameter("tax_rate");
        String discountRateStr = request.getParameter("discount_rate");
        String customerEmail = request.getParameter("customer_email");

        // service layer to handle the booking creation logic
        String result = bookingService.addBooking(
                customerName, customerAddress, phoneNumber, destination, carIdStr, scheduledDateStr,
                scheduledTimeStr, fareType, taxRateStr, discountRateStr, customerEmail, customerRegNumber
        );

        out.write(result);
        if (result.startsWith("success")) {
            LOGGER.log(Level.INFO, "Booking created successfully with Order Number: " + result.split(":")[1]);
        } else {
            LOGGER.log(Level.SEVERE, "Failed to create booking: " + result);
        }
    }
}
