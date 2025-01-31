package com.example.cabbookingsystem.factory;

import com.example.cabbookingsystem.model.User;
import com.example.cabbookingsystem.util.PasswordUtil;

public class UserFactory {
    public static User createUser(String customerRegNum, String fullName, String address, String nic, String email, String password) {
        String hashedPassword = PasswordUtil.hashPassword(password);
        return new User(customerRegNum, fullName, address, nic, email, hashedPassword);
    }
}
