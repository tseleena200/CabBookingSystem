package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.service.UserService;
import com.example.cabbookingsystem.model.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/editEmployee")
public class EditEmployeeServlet extends HttpServlet {
    private final UserService userService = new UserService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerRegistrationNumber = request.getParameter("customerRegistrationNumber");

        // Use the service layer to get the employee by their customer registration number
        User employee = userService.getEmployeeByCustomerRegistrationNumber(customerRegistrationNumber);

        if (employee == null) {
            response.sendRedirect("manage_employees.jsp");
            return;
        }

        // Set the employee object in the request attributes to be used in the JSP
        request.setAttribute("employee", employee);

        // Forward the request to the edit employee JSP page
        request.getRequestDispatcher("/admin/edit_employee.jsp").forward(request, response);
    }
}
