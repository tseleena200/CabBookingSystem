package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.UserDAO;
import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.util.PasswordUtil;

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
    private final UserDAO userDAO = UserDAO.getInstance(); // Singleton DAO

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Fetch User from Database
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            System.out.println("No user found for email: " + email);
            response.getWriter().write("failure");
            return;
        }


        String inputHashedPassword = PasswordUtil.hashPassword(password);
        System.out.println(" Hashed Input Password: " + inputHashedPassword);
        System.out.println(" Stored Hash in DB: " + user.getPassword());

        // Compare Hashed Passwords
        if (inputHashedPassword.equals(user.getPassword())) {
            System.out.println(" Login successful!");

            // Start session to keep user logged in
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            response.getWriter().write("success");

        } else {
            System.out.println("Password mismatch for email: " + email);
            response.getWriter().write("failure");
        }
    }
}
