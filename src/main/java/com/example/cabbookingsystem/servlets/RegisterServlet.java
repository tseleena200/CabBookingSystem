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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = UserDAO.getInstance(); // Singleton DAO

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get existing session (don't create a new one)

        if (session == null || session.getAttribute("user") == null) {
            System.out.println("⚠️ Unauthorized Access: No Admin Session Found");
            response.getWriter().write("unauthorized"); // Send unauthorized response
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        // ✅ Ensure only the ADMIN can register employees
        if (!"admin@megacitycab.com".equals(currentUser.getEmail())) {
            System.out.println("⚠️ Unauthorized Access: User is not an Admin");
            response.getWriter().write("unauthorized");
            return;
        }

        // ✅ Extract Employee Registration Details
        String customerRegNum = request.getParameter("customer_registration_number");
        String fullName = request.getParameter("full_name");
        String address = request.getParameter("address");
        String nic = request.getParameter("nic_number");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("📌 Registering Employee: " + fullName + " | Email: " + email);

        // ✅ Create Employee Object
        User user = UserFactory.createUser(customerRegNum, fullName, address, nic, email, password, "employee");

        // ✅ Save Employee to Database
        if (userDAO.registerUser(user)) {
            System.out.println("✅ Employee Registered Successfully: " + email);
            response.getWriter().write("success"); // Send success response
        } else {
            System.out.println("❌ Registration FAILED for: " + email);
            response.getWriter().write("failure"); // Send failure response
        }
    }
}
