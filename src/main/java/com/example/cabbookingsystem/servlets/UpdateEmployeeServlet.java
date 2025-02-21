package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/updateEmployee")
public class UpdateEmployeeServlet extends HttpServlet {
    private final UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerRegistrationNumber = request.getParameter("customerRegistrationNumber");
        String fullName = request.getParameter("fullName");
        String address = request.getParameter("address");
        String nicNumber = request.getParameter("nicNumber");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        // Set default role if not provided
        if (role == null || role.isEmpty()) {
            role = "employee"; // Default to 'employee' if no role is provided
        }

        // Use the service layer to update the employee
        String result = userService.updateEmployee(customerRegistrationNumber, fullName, address, nicNumber, email, role);

        if ("success".equals(result)) {
            // Set update status as success before redirect
            request.setAttribute("updateStatus", "success");
            request.getRequestDispatcher("manage_employees.jsp").forward(request, response); // Forward to manage employees page
        } else {
            // Set update status as failure before redirect
            request.setAttribute("updateStatus", "failure");
            request.getRequestDispatcher("manage_employees.jsp").forward(request, response); // Forward to manage employees page
        }
    }
}
