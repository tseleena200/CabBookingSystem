package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/EditDriverServlet")
public class EditDriverServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DriverService driverService = new DriverService(); // Use the service layer

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int driverId = Integer.parseInt(request.getParameter("driver_id"));
            String fullName = request.getParameter("full_name");
            String contactNumber = request.getParameter("contact_number");
            String licenseNumber = request.getParameter("license_number");

            // Create Updated Driver Object
            String result = driverService.updateDriver(driverId, fullName, licenseNumber, contactNumber); // Call service layer method

            if ("success".equals(result)) {
                response.sendRedirect("manage_drivers_admin.jsp?success=driver_updated");
            } else {
                response.sendRedirect("manage_drivers_admin.jsp?error=" + result);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("manage_drivers_admin.jsp?error=invalid_driver_id");
        }
    }
}
