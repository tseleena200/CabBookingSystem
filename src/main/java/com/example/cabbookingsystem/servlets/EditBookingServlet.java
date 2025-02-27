package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;

@WebServlet("/editBooking")
public class EditBookingServlet extends HttpServlet {
    private final BookingService bookingService = new BookingService(); // Use the service layer

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        // Retrieve session and validate user authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {

            response.getWriter().write("unauthorized");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");

        // Allow both employees and admins to edit bookings
        if (!"employee".equals(loggedInUser.getRole()) && !"admin".equals(loggedInUser.getRole())) {
            response.getWriter().write("unauthorized");
            return;
        }

        try {
            String orderNumber = request.getParameter("order_number");
            String customerName = request.getParameter("customer_name");
            String address = request.getParameter("address");
            String telephone = request.getParameter("telephone");
            String destination = request.getParameter("destination");
            String scheduledDate = request.getParameter("scheduled_date");
            String scheduledTime = request.getParameter("scheduled_time");
            String fareType = request.getParameter("fare_type");
            String customerEmail = request.getParameter("customer_email");

            double baseFare = request.getParameter("base_fare") != null ? Double.parseDouble(request.getParameter("base_fare")) : 0.0;
            double distance = request.getParameter("distance") != null ? Double.parseDouble(request.getParameter("distance")) : 0.0;
            double taxRate = request.getParameter("tax_rate") != null ? Double.parseDouble(request.getParameter("tax_rate")) : 0.0;
            double discountRate = request.getParameter("discount_rate") != null ? Double.parseDouble(request.getParameter("discount_rate")) : 0.0;


            if (orderNumber == null || orderNumber.trim().isEmpty()) {
                response.getWriter().write("error_missing_order");
                return;
            }

            // Use the BookingService to update the booking
            String result = bookingService.updateBooking(orderNumber, customerName, address, telephone, destination,
                    scheduledDate, scheduledTime, fareType, baseFare, distance, taxRate, discountRate, customerEmail);

            if ("success".equals(result)) {
                response.getWriter().write("success");
            } else {
                response.getWriter().write(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("error_exception");
        }
    }
}
