package com.example.cabbookingsystem.service;

import com.example.cabbookingsystem.dao.UserDAO;
import com.example.cabbookingsystem.factory.UserFactory;
import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.util.PasswordUtil;

import java.util.regex.Pattern;

public class UserService {
    private final UserDAO userDAO = UserDAO.getInstance();
    // Method to register a new user
    public String registerUser(String customerRegNum, String fullName, String address, String nic, String email, String password) {
        // Validation checks
        if (customerRegNum == null || fullName == null || address == null || nic == null || email == null || password == null ||
                customerRegNum.isEmpty() || fullName.isEmpty() || address.isEmpty() || nic.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return "missing_fields";
        }

        if (!Pattern.matches("^\\d{10}|\\d{12}$", nic)) {
            return "invalid_nic";
        }

        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", password)) {
            return "weak_password";
        }
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            return "invalid_email_format";
        }
        if (userDAO.isDuplicateEmail(email)) {
            return "duplicate_email";
        }
        if (userDAO.isDuplicateCustomerRegNum(customerRegNum)) {
            return "duplicate_customer_reg_num";
        }
        if (userDAO.isDuplicateNIC(nic)) {
            return "duplicate_nic";
        }

        // Create user using factory
        User user = UserFactory.createUser(customerRegNum, fullName, address, nic, email, password, "employee");

        // Register the user in the database
        if (userDAO.registerUser(user)) {
            return "success";
        }

        return "failure"; // Registration failed
    }
    // Method to check if email is duplicate
    public boolean isDuplicateEmail(String email) {
        return userDAO.isDuplicateEmail(email); // Call to DAO to check email
    }

    // Method to check if customer registration number is duplicate
    public boolean isDuplicateCustomerRegNum(String customerRegNum) {
        return userDAO.isDuplicateCustomerRegNum(customerRegNum); // Call to DAO to check customer registration number
    }

    // Method to check if NIC is duplicate
    public boolean isDuplicateNIC(String nic) {
        return userDAO.isDuplicateNIC(nic); // Call to DAO to check NIC
    }

    // Method to handle login logic
    public String loginUser(String email, String password) {
        // Fetch user by email
        User user = getUserByEmail(email);
        if (user == null) {
            return "failure";
        }

        // Hash input password for comparison
        String inputHashedPassword = PasswordUtil.hashPassword(password);

        // Compare passwords
        if (inputHashedPassword.equals(user.getPassword())) {
            return user.getRole(); // Return role if login is successful
        }

        return "failure"; // Password mismatch
    }
    // method to delete an employee
    public String deleteEmployee(String customerRegNum) {
        boolean deleted = userDAO.deleteEmployee(customerRegNum);

        if (deleted) {
            return "Employee deleted successfully";
        } else {
            return "Failed to delete employee";
        }
    }
    // Method to update employee details
    public String updateEmployee(String customerRegistrationNumber, String fullName, String address, String nicNumber, String email, String role) {
        // Call DAO to update employee in the database
        boolean updated = userDAO.updateEmployee(customerRegistrationNumber, fullName, address, nicNumber, email, role);
        return updated ? "success" : "update_failed";
    }

    // Method to get employee by customer registration number
    public User getEmployeeByCustomerRegistrationNumber(String customerRegistrationNumber) {
        return userDAO.getEmployeeByCustomerRegistrationNumber(customerRegistrationNumber); // Delegate to DAO
    }

    // Method to get user by email
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email); // Fetch user from the DAO layer
    }
}
