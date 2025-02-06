package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.BookingDAO;
import com.example.cabbookingsystem.factory.BookingFactory;
import com.example.cabbookingsystem.model.Booking;
import com.example.cabbookingsystem.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/addBooking")
public class AddBookingServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddBookingServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        //  Check if the user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            out.write("unauthorized");
            LOGGER.log(Level.WARNING, "Unauthorized access attempt.");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String customerRegNumber = currentUser.getCustomerRegistrationNumber();

        //  Retrieve form data
        String customerName = request.getParameter("customer_name");
        String customerAddress = request.getParameter("customer_address");
        String phoneNumber = request.getParameter("phone_number");
        String destination = request.getParameter("destination");
        String carIdStr = request.getParameter("car_id");
        String scheduledDateStr = request.getParameter("scheduled_date");
        String scheduledTimeStr = request.getParameter("scheduled_time");

        LOGGER.log(Level.INFO, "📌 Processing new booking for {0}", customerName);

        //  Validate required fields
        if (customerName == null || customerAddress == null || phoneNumber == null || destination == null ||
                carIdStr == null || scheduledDateStr == null || scheduledTimeStr == null ||
                customerName.isEmpty() || customerAddress.isEmpty() || phoneNumber.isEmpty() ||
                destination.isEmpty() || carIdStr.isEmpty() || scheduledDateStr.isEmpty() || scheduledTimeStr.isEmpty()) {

            out.write("missing_fields");
            LOGGER.log(Level.WARNING, "⚠️ Missing fields in booking form.");
            return;
        }

        int carId;
        try {
            carId = Integer.parseInt(carIdStr);
        } catch (NumberFormatException e) {
            out.write("invalid_car");
            LOGGER.log(Level.WARNING, "⚠️ Invalid car ID provided.");
            return;
        }

        Date scheduledDate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            scheduledDate = dateFormat.parse(scheduledDateStr);
        } catch (ParseException e) {
            out.write("invalid_date");
            LOGGER.log(Level.WARNING, "⚠️ Invalid date format provided.");
            return;
        }

        //  Validate Scheduled Time Format (HH:mm)
        if (!scheduledTimeStr.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            out.write("invalid_time");
            LOGGER.log(Level.WARNING, "⚠️ Invalid time format provided.");
            return;
        }

        //  Retrieve driver ID
        int driverId = BookingDAO.getInstance().getDriverIdByCarId(carId);
        if (driverId == -1) {
            out.write("driver_not_found");
            LOGGER.log(Level.WARNING, "⚠️ No driver found for car ID: {0}", carId);
            return;
        }

        //  Check if car is already booked at the same time
        if (BookingDAO.getInstance().isCarBooked(carId, new java.sql.Date(scheduledDate.getTime()), scheduledTimeStr)) {
            out.write("car_already_booked");
            LOGGER.log(Level.WARNING, "⚠️ Car ID {0} is already booked for {1} at {2}", new Object[]{carId, scheduledDate, scheduledTimeStr});
            return;
        }

        //  Generate unique Order Number
        String orderNumber = "ORD-" + System.currentTimeMillis();

        //  Create booking using Factory Pattern
        Booking newBooking = BookingFactory.createBooking(
                orderNumber, customerName, customerAddress, phoneNumber, destination,
                new Timestamp(System.currentTimeMillis()),
                scheduledDate, scheduledTimeStr,
                carId, driverId, customerRegNumber, null
        );
        //  Set default total amount
        newBooking.setTotalAmount(0.0);

        //  Save the booking
        boolean success = BookingDAO.getInstance().addBooking(newBooking);

        if (success) {
            out.write("success:" + newBooking.getOrderNumber());
            LOGGER.log(Level.INFO, " Booking created successfully with Order Number: {0}", newBooking.getOrderNumber());
        } else {
            out.write("db_insert_fail");
            LOGGER.log(Level.SEVERE, " Failed to insert booking into database.");
        }
    }
}
