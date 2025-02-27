package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/deleteEmployee")
public class DeleteEmployeeServlet extends HttpServlet {
    private final UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerRegistrationNumber = request.getParameter("customerRegistrationNumber");

        if (customerRegistrationNumber != null && !customerRegistrationNumber.isEmpty()) {
            String result = userService.deleteEmployee(customerRegistrationNumber);
            request.setAttribute("operationResult", result);
            request.getRequestDispatcher("manage_employees.jsp").forward(request, response);
        } else {
            response.getWriter().write("Invalid employee registration number");
        }
    }
}

