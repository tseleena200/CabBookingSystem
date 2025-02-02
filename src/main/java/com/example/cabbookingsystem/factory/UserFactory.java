package com.example.cabbookingsystem.factory;

import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.util.PasswordUtil;

public class UserFactory {

    // ✅ Ensures password is hashed consistently and role is set correctly
    public static User createUser(String customerRegNum, String fullName, String address, String nic, String email, String password, String role) {
        String hashedPassword = PasswordUtil.hashPassword(password); // ✅ Always hash before saving

        // ✅ Ensure only "employee" or "admin" is allowed
        if (!role.equals("employee") && !role.equals("admin")) {
            System.out.println("⚠️ [UserFactory] Invalid role assigned! Defaulting to employee.");
            role = "employee"; // Default to employee if invalid role is passed
        }

        System.out.println("✅ [UserFactory] Creating User: " + fullName + " | Role: " + role);
        return new User(customerRegNum, fullName, address, nic, email, hashedPassword, role);
    }
}
