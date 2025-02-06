package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.BookingDAO;
import com.example.cabbookingsystem.model.Booking;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/bookingConfirmation")
public class BookingConfirmationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BookingDAO bookingDAO = BookingDAO.getInstance();
    private static final Logger LOGGER = Logger.getLogger(BookingConfirmationServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning("Unauthorized access attempt - No user session found.");
            response.getWriter().write("unauthorized");
            return;
        }

        String customerRegNum = (String) session.getAttribute("userRegNum");
        if (customerRegNum == null) {
            response.getWriter().write("unauthorized");
            return;
        }

        int bookingId = Integer.parseInt(request.getParameter("booking_id"));
        boolean success = bookingDAO.updateBookingStatus(bookingId, "Confirmed", customerRegNum);

        response.getWriter().write(success ? "success" : "failure");
    }
}
