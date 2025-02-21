package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/check_duplicate")
public class DuplicateCheckServlet extends HttpServlet {
    private final UserService userService = new UserService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String field = request.getParameter("field");
        String value = request.getParameter("value");

        boolean isDuplicate = false;

        switch (field) {
            case "customer_registration_number":
                isDuplicate = userService.isDuplicateCustomerRegNum(value);
                break;
            case "nic_number":
                isDuplicate = userService.isDuplicateNIC(value);
                break;
            case "email":
                isDuplicate = userService.isDuplicateEmail(value);
                break;
        }

        response.getWriter().write(isDuplicate ? "duplicate" : "unique");
    }
}
