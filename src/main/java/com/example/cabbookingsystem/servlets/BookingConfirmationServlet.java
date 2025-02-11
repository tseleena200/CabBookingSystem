package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.BookingDAO;
import com.example.cabbookingsystem.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/bookingConfirmation")
public class BookingConfirmationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BookingConfirmationServlet.class.getName());
    private final BookingDAO bookingDAO = BookingDAO.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        //  Retrieve session and validate user authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning(" Unauthorized access attempt - No user session found.");
            response.getWriter().write("unauthorized");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");
        if (!"employee".equals(loggedInUser.getRole())) {
            LOGGER.warning(" Unauthorized access - User is not an employee.");
            response.getWriter().write("unauthorized");
            return;
        }

        String employeeRegNum = loggedInUser.getCustomerRegistrationNumber();
        if (employeeRegNum == null || employeeRegNum.isEmpty()) {
            LOGGER.warning(" Employee registration number is missing.");
            response.getWriter().write("invalid_employee");
            return;
        }

        //  Validate booking order number (now treated as String)
        String orderNumber = request.getParameter("order_number");
        if (orderNumber == null || orderNumber.isEmpty()) {
            LOGGER.warning(" Missing order number in request.");
            response.getWriter().write("missing_order_number");
            return;
        }

        //  Check if the booking exists and its current status
        String currentStatus = bookingDAO.getBookingStatus(orderNumber);
        if (currentStatus == null) {
            LOGGER.warning(" Booking Order " + orderNumber + " does not exist.");
            response.getWriter().write("booking_not_found");
            return;
        }

        if ("Confirmed".equalsIgnoreCase(currentStatus)) {
            LOGGER.warning(" Booking Order " + orderNumber + " is already confirmed.");
            response.getWriter().write("already_confirmed");
            return;
        }

        //  Update booking status to "Confirmed"
        boolean success = bookingDAO.updateBookingStatus(orderNumber, "Confirmed", employeeRegNum);

        if (success) {
            LOGGER.log(Level.INFO, " Booking Order {0} confirmed by Employee {1}", new Object[]{orderNumber, employeeRegNum});
            response.getWriter().write("success");
        } else {
            LOGGER.severe(" Failed to confirm Booking Order " + orderNumber);
            response.getWriter().write("update_failed");
        }
    }
}