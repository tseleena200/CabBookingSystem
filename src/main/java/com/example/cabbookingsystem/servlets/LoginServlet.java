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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService(); // Service layer instance

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Delegate login logic to UserService
        String result = userService.loginUser(email, password);

        if ("failure".equals(result)) {
            response.getWriter().write("failure");
            return;
        }

        // Start user session and redirect based on role
        HttpSession session = request.getSession();
        User user = userService.getUserByEmail(email);
        session.setAttribute("user", user);
        session.setAttribute("user_role", result);

        if ("admin".equals(result)) {
            response.getWriter().write("admin");
        } else {
            response.getWriter().write("employee");
        }
    }
}
