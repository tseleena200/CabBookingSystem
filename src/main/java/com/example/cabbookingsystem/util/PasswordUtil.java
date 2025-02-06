package com.example.cabbookingsystem.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    //  Method to Generate Hashed Password for Admin
    public static void main(String[] args) {
        String adminPassword = "admin123";  // Set the current admin password
        String hashedPassword = hashPassword(adminPassword); // Hash it
        System.out.println("🔒 Hashed Admin Password: " + hashedPassword);
    }
}
