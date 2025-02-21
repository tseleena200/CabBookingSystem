package com.example.cabbookingsystem.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate the current session
        HttpSession userSession = request.getSession(false);
        if (userSession != null) {
            userSession.invalidate(); // Invalidate the session
        }

        // Redirect to the login page with the message parameter
        response.sendRedirect("../html/login.html?message=logged_out");
    }
}
