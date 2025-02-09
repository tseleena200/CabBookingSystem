package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.DriverDAO;
import com.example.cabbookingsystem.model.Driver;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/EditDriverServlet")
public class EditDriverServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DriverDAO driverDAO = DriverDAO.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int driverId = Integer.parseInt(request.getParameter("driver_id"));
            String fullName = request.getParameter("full_name");
            String contactNumber = request.getParameter("contact_number");
            String licenseNumber = request.getParameter("license_number");

            // Create Updated Driver Object
            Driver driver = new Driver(driverId, fullName, licenseNumber, contactNumber);

            boolean updated = driverDAO.updateDriver(driver);

            if (updated) {
                //  Redirect with success message
                response.sendRedirect("manage_drivers_admin.jsp?success=driver_updated");
            } else {
                response.sendRedirect("manage_drivers_admin.jsp?error=update_failed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("manage_drivers_admin.jsp?error=invalid_driver_id");
        }
    }
}
