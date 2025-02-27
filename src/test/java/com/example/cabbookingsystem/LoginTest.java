package com.example.cabbookingsystem;

import com.example.cabbookingsystem.service.UserService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private final UserService userService = new UserService();

    @Test
    public void testValidAdminLogin() {
        // Simulating the login logic
        String email = "admin@megacitycab.com";
        String password = "admin123";

        // Assume loginUser is a method that attempts login and returns the result as "admin" or "failure"
        String result = userService.loginUser(email, password);


        assertEquals("admin", result, "Admin login should succeed and return admin role.");
    }
    @Test
    public void testInvalidAdminLogin() {
        // Simulating the login logic
        String email = "wrongemail@megacitycab.com";
        String password = "Admin123";

        // Assume loginUser is a method that attempts login and returns the result as "failure"
        String result = userService.loginUser(email, password);

        // Assert the expected result for invalid login
        assertEquals("failure", result, "Admin login should fail with invalid credentials");
    }
    @Test
    public void testValidEmployeeLogin() {
        String email = "employee@megacitycab.com";
        String password = "Employee123";

        // Assume loginUser is a method that attempts login and returns the result as "employee" or "failure"
        String result = userService.loginUser(email, password);

        // Assert the expected result
        assertEquals("employee", result, "Employee login should succeed and return employee role.");
    }
    @Test
    public void testInvalidEmployeeLogin() {
        // Simulating the login logic
        String email = "wrongemail@megacitycab.com";
        String password = "Employee123";

        String result = userService.loginUser(email, password);

        assertEquals("failure", result, "Invalid employee login should return failure.");
    }
    @Test
    public void testEmptyEmailOrPassword() {
        // Simulating the login logic with an empty email
        String email = "";
        String password = "Employee123";

        // Assume loginUser is a method that attempts login and returns the result as "failure" when fields are empty
        String result = userService.loginUser(email, password);

        assertEquals("failure", result, "Login should fail when email is empty.");

        // Simulating the login logic with an empty password
        email = "employee@megacitycab.com";
        password = "";

        result = userService.loginUser(email, password);

        assertEquals("failure", result, "Login should fail when password is empty.");
    }
}
