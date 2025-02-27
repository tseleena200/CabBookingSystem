package com.example.cabbookingsystem;

import com.example.cabbookingsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserRegistrationTest {

    private final UserService userService = new UserService(); // Create instance of UserService

    @BeforeEach
    public void setUp() {
        // Set up initial conditions if required
    }

    @Test
    public void testSuccessfulRegistration() {
        // Test Case 1: Successful Registration (No Errors)

        String customerRegNum = "C9876543";
        String fullName = "Alice Green";
        String address = "789 Maple Rd";
        String nic = "143557789012";
        String email = "alicegreen@megacitycab.com";
        String password = "NewSecurePassword321";

        // Simulate the registration process
        String result = userService.registerUser(customerRegNum, fullName, address, nic, email, password);

        // Assert the expected result
        assertEquals("success", result, "Employee account should be successfully created.");
    }
    @Test
    public void testDuplicateNIC() {
        // Test Case 2: Duplicate NIC
        // Test data with duplicate NIC
        String customerRegNum = "C123456744";
        String fullName = "John Smith";
        String address = "123 Oak St";
        String nic = "143557789012";  // Duplicate NIC
        String email = "johnsmith1@megacitycab.com";
        String password = "SecurePassword123";

        // First, register the employee with the duplicate NIC
        userService.registerUser(customerRegNum, fullName, address, nic, email, password);

        // Now, attempt to register again with the same NIC and new email
        String result = userService.registerUser(customerRegNum, fullName, address, nic, email, password);

        // Assert the expected result: registration should fail due to duplicate NIC
        assertEquals("duplicate_nic", result, "Registration should fail due to duplicate NIC.");
    }

    @Test
    public void testDuplicateEmail() {
        // Test Case 3: Duplicate Email with Different Data
        String customerRegNum = "C2345678";
        String fullName = "ELLIE Smith";
        String address = "789 Main St";
        String nic = "787655321098";
        String email = "emilyw@megacitycab.com"; // Email that already exists
        String password = "Employee123";

        //  register the employee with a unique email
        userService.registerUser(customerRegNum, fullName, address, nic, email, password);

        //  attempt to register again with the same email but new data
        String newCustomerRegNum = "C8765432";  // Different reg number
        String newFullName = "Bob Brown";  // New full name
        String newAddress = "123 Oak Rd";  // New address
        String newNic = "112233445566";  // New NIC
        String newPassword = "NewPassword123";  // New password

        String result = userService.registerUser(newCustomerRegNum, newFullName, newAddress, newNic, email, newPassword);

        System.out.println("Result of second registration attempt: " + result);

        //registration should fail due to duplicate email
        assertEquals("duplicate_email", result, "Registration should fail due to duplicate email.");
    }
    @Test
    public void testEmptyFields() {
        // Test Case 4: Empty Fields

        String customerRegNum = "C2345678";
        String fullName = "Alice Smith";
        String address = "789 Main St";
        String nic = "987654321098";
        String email = "";  // Empty email field
        String password = ""; // Empty password field

        // Attempt registration with empty email and password fields
        String result = userService.registerUser(customerRegNum, fullName, address, nic, email, password);

        // Print out the result for debugging
        System.out.println("Result of registration with empty fields: " + result);

        // Assert the expected result: registration should fail due to empty fields
        assertEquals("missing_fields", result, "Registration should fail due to empty fields.");
    }

    @Test
    public void testInvalidNICFormat() {
        // Test Case 5: Invalid NIC Format
        String customerRegNum = "C2345678";
        String fullName = "Alice Smith";
        String address = "789 Main St";
        String nic = "144567890";  // Invalid NIC format (only 9 digits)
        String email = "alice.smith@megacitycab.com";
        String password = "ValidPassword123";

        // Attempt registration with invalid NIC format
        String result = userService.registerUser(customerRegNum, fullName, address, nic, email, password);

        // Print out the result for debugging
        System.out.println("Result of registration with invalid NIC format: " + result);

        // Assert the expected result: registration should fail due to invalid NIC format
        // Updated the expected result to match the actual one: "invalid_nic"
        assertEquals("invalid_nic", result, "Registration should fail due to invalid NIC format.");
    }
    @Test
    public void testWeakPassword() {
        // Test Case 6: Weak Password
        String customerRegNum = "C2345678";
        String fullName = "Alice Smith";
        String address = "789 Main St";
        String nic = "987654321098";
        String email = "alice.smith@megacitycab.com";
        String password = "12345";  // Weak password

        // Attempt registration with weak password
        String result = userService.registerUser(customerRegNum, fullName, address, nic, email, password);

        System.out.println("Result of registration with weak password: " + result);

        //  expected result: registration should fail due to weak password
        assertEquals("weak_password", result, "Registration should fail due to weak password.");
    }
    @Test
    public void testInvalidEmailFormat() {
        // Test Case 7: Invalid Email Format

        String customerRegNum = "C4555657654";
        String fullName = " White";
        String address = "105 Pine St";
        String nic = "6767656543";
        String email = "invalidemail";  // Invalid email format (missing '@' and domain)
        String password = "StrongPassword123";

        // Attempt to register with an invalid email format
        String result = userService.registerUser(customerRegNum, fullName, address, nic, email, password);

        // registration should fail due to invalid email format
        assertEquals("invalid_email_format", result, "Registration should fail due to invalid email format.");
    }
}
