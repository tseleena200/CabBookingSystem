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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/deleteBooking")
public class DeleteBookingServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DeleteBookingServlet.class.getName());
    private final BookingService bookingService = new BookingService();  // Use BookingService

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        // Retrieve session and validate user authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning(" Unauthorized access attempt - No user session found.");
            response.getWriter().write("unauthorized");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");

        // Allow both employees and admins to delete bookings
        if (!"employee".equals(loggedInUser.getRole()) && !"admin".equals(loggedInUser.getRole())) {
            LOGGER.warning(" Unauthorized access - User is neither employee nor admin.");
            response.getWriter().write("unauthorized");
            return;
        }

        String employeeRegNum = loggedInUser.getCustomerRegistrationNumber();
        if (employeeRegNum == null || employeeRegNum.isEmpty()) {
            LOGGER.warning(" Employee registration number is missing.");
            response.getWriter().write("invalid_employee");
            return;
        }

        // Validate booking order number
        String orderNumber = request.getParameter("order_number");
        if (orderNumber == null || orderNumber.isEmpty()) {
            LOGGER.warning(" Missing order number in request.");
            response.getWriter().write("missing_order_number");
            return;
        }

        // Use BookingService to delete the booking
        String result = bookingService.deleteBooking(orderNumber);

        // Respond with the result
        response.getWriter().write(result);
    }
}
