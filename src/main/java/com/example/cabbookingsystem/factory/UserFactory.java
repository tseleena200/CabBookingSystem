package com.example.cabbookingsystem.factory;

import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.util.PasswordUtil;

public class UserFactory {


    public static User createUser(String customerRegNum, String fullName, String address, String nic, String email, String password, String role) {
        String hashedPassword = PasswordUtil.hashPassword(password);


        if (!role.equals("employee") && !role.equals("admin")) {
            System.out.println("⚠️ [UserFactory] Invalid role assigned! Defaulting to employee.");
            role = "employee";
        }

        System.out.println("✅ [UserFactory] Creating User: " + fullName + " | Role: " + role);
        return new User(customerRegNum, fullName, address, nic, email, hashedPassword, role);
    }
}
