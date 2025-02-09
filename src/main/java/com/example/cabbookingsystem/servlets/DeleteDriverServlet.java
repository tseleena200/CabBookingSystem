package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.DriverDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/DeleteDriverServlet")
public class DeleteDriverServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DriverDAO driverDAO = DriverDAO.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String driverIdStr = request.getParameter("driver_id");

        if (driverIdStr != null) {
            try {
                int driverId = Integer.parseInt(driverIdStr);

                // 🚕 Delete Driver from Database
                boolean deleted = driverDAO.deleteDriver(driverId);

                if (deleted) {
                    response.sendRedirect("manage_drivers_admin.jsp?success=driver_deleted");
                } else {
                    response.sendRedirect("manage_drivers_admin.jsp?error=delete_failed");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("manage_drivers_admin.jsp?error=invalid_driver_id");
            }
        }
    }
}
