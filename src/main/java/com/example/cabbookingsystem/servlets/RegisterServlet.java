package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.UserDAO;
import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.factory.UserFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = UserDAO.getInstance(); // Singleton DAO

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerRegNum = request.getParameter("customer_registration_number");
        String fullName = request.getParameter("full_name");
        String address = request.getParameter("address");
        String nic = request.getParameter("nic_number");
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        User user = UserFactory.createUser(customerRegNum, fullName, address, nic, email, password);

        if (userDAO.registerUser(user)) {
            response.sendRedirect("html/login.html"); // Redirect to login page
        } else {
            response.sendRedirect("html/register.html?error=db_insert_fail"); // Redirect back with error
        }
    }
}
