package com.example.cabbookingsystem.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.cabbookingsystem.dao.BookingDAO;

@WebServlet("/updateDriver")
public class UpdateDriverServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderNumber = request.getParameter("order_number");
        String driverIdStr = request.getParameter("driver_id");

        System.out.println("[DEBUG] Received order_number: " + orderNumber);
        System.out.println("[DEBUG] Received driver_id: " + driverIdStr);

        if (orderNumber == null || driverIdStr == null || orderNumber.isEmpty() || driverIdStr.isEmpty()) {
            System.out.println("[ERROR] Missing parameters");
            response.getWriter().write("error");
            return;
        }

        int driverId;
        try {
            driverId = Integer.parseInt(driverIdStr);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid driver ID");
            response.getWriter().write("error");
            return;
        }

        BookingDAO bookingDAO = BookingDAO.getInstance();
        boolean success = bookingDAO.updateDriverForBooking(orderNumber, driverId);

        if (success) {
            response.getWriter().write("success");
        } else {
            response.getWriter().write("error");
        }
    }
}
