package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService(); // Service layer instance

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            System.out.println(" Unauthorized Access: No Admin Session Found");
            response.getWriter().write("unauthorized");
            return;
        }

        // Ensure the current user is an admin
        User currentUser = (User) session.getAttribute("user");

        if (!"admin@megacitycab.com".equals(currentUser.getEmail())) {
            System.out.println(" Unauthorized Access: User is not an Admin");
            response.getWriter().write("unauthorized");
            return;
        }
        //  form data
        String customerRegNum = request.getParameter("customer_registration_number");
        String fullName = request.getParameter("full_name");
        String address = request.getParameter("address");
        String nic = request.getParameter("nic_number");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // registration logic to UserService
        String result = userService.registerUser(customerRegNum, fullName, address, nic, email, password);

        // Respond based on result from service layer
        response.getWriter().write(result);
    }
}
