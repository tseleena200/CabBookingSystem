package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.UserDAO;
import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.factory.UserFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = UserDAO.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            System.out.println(" Unauthorized Access: No Admin Session Found");
            response.getWriter().write("unauthorized");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        if (!"admin@megacitycab.com".equals(currentUser.getEmail())) {
            System.out.println(" Unauthorized Access: User is not an Admin");
            response.getWriter().write("unauthorized");
            return;
        }

        String customerRegNum = request.getParameter("customer_registration_number");
        String fullName = request.getParameter("full_name");
        String address = request.getParameter("address");
        String nic = request.getParameter("nic_number");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println(" Registering Employee: " + fullName + " | Email: " + email);

        if (customerRegNum == null || fullName == null || address == null || nic == null || email == null || password == null ||
                customerRegNum.isEmpty() || fullName.isEmpty() || address.isEmpty() || nic.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println(" Validation Failed: Missing Required Fields");
            response.getWriter().write("missing_fields");
            return;
        }

        if (!Pattern.matches("^\\d{10}|\\d{12}$", nic)) {
            System.out.println(" Validation Failed: Invalid NIC Format");
            response.getWriter().write("invalid_nic");
            return;
        }

        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", password)) {
            System.out.println(" Validation Failed: Weak Password");
            response.getWriter().write("weak_password");
            return;
        }

        if (userDAO.isDuplicateEmail(email)) {
            System.out.println(" Registration FAILED: Duplicate Email");
            response.getWriter().write("duplicate_email");
            return;
        }
        if (userDAO.isDuplicateCustomerRegNum(customerRegNum)) {
            System.out.println(" Registration FAILED: Duplicate Registration Number");
            response.getWriter().write("duplicate_customer_reg_num");
            return;
        }
        if (userDAO.isDuplicateNIC(nic)) {
            System.out.println(" Registration FAILED: Duplicate NIC Number");
            response.getWriter().write("duplicate_nic");
            return;
        }

        User user = UserFactory.createUser(customerRegNum, fullName, address, nic, email, password, "employee");

        if (userDAO.registerUser(user)) {
            System.out.println(" Employee Registered Successfully: " + email);
            response.getWriter().write("success");
        } else {
            System.out.println(" Registration FAILED for: " + email);
            response.getWriter().write("failure");
        }
    }
}
