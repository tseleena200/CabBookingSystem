package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.UserDAO;
import com.example.cabbookingsystem.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAO(); // DAO instance

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Debugging: Print received form values
        System.out.println("📩 Received Registration Data:");
        System.out.println("Customer Reg Num: " + request.getParameter("customer_registration_number"));
        System.out.println("Full Name: " + request.getParameter("full_name"));
        System.out.println("Address: " + request.getParameter("address"));
        System.out.println("NIC: " + request.getParameter("nic_number"));
        System.out.println("Email: " + request.getParameter("email"));
        System.out.println("Password: " + request.getParameter("password"));

        String customerRegNum = request.getParameter("customer_registration_number");
        String fullName = request.getParameter("full_name");
        String address = request.getParameter("address");
        String nic = request.getParameter("nic_number");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (customerRegNum == null || fullName == null || email == null || password == null) {
            System.err.println("ERROR: Missing form data. Check form names in HTML.");
            response.sendRedirect(request.getContextPath() + "/html/register.html?error=missing_data");
            return;
        }

        User user = new User(customerRegNum, fullName, address, nic, email, password);

        if (userDAO.registerUser(user)) {
            response.sendRedirect(request.getContextPath() + "/html/register_success.html");
        } else {
            response.sendRedirect(request.getContextPath() + "/html/register.html?error=db_insert_fail");
        }
    }
}
