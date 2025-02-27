package com.example.cabbookingsystem;

import com.example.cabbookingsystem.dao.BookingDAO;
import com.example.cabbookingsystem.model.Booking;
import com.example.cabbookingsystem.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    private final BookingService bookingService = new BookingService();
    private final BookingDAO bookingDAO = BookingDAO.getInstance();// Create instance of BookingService

    @BeforeEach
    public void setUp() {
        // Set up initial conditions if required
    }

    @Test
    public void testValidBooking() {
        // Test Case 1: Valid Booking

        // Test data with valid booking details
        String customerName = "John Doe";
        String customerEmail = "johndoe@example.com";
        String customerAddress = "789 Maple St";
        String telephone = "0987654321";
        String destination = "123 Pine Ave";
        String scheduledDate = "2025-03-15";
        String scheduledTime = "10:30";
        String carIdStr = "2";
        String fareType = "Luxury";

        // Call the addBooking method to create the booking
        String result = bookingService.addBooking(customerName, customerAddress, telephone, destination,
                carIdStr, scheduledDate, scheduledTime, fareType,
                "5", "10", customerEmail, "D12345678");

        // Assert that the booking is successfully created by checking if the result starts with "success:"
        assertTrue(result.startsWith("success:"),
                "Expected booking creation to be successful but got: " + result);
    }
    @Test
    public void testScheduledDateValidation() {
        // Test Case 2: Scheduled Date Validation

        // Test data with invalid scheduled date (past date)
        String customerName = "John Doe";
        String customerEmail = "johndoe@example.com";
        String customerAddress = "123 Main St";
        String telephone = "987654321";
        String destination = "456 Oak Ave";
        String scheduledDate = "2024-01-01";  // Past date
        String scheduledTime = "14:00";
        String carIdStr = "2";
        String fareType = "Standard";

        // Call the addBooking method to attempt booking with a past date
        String result = bookingService.addBooking(customerName, customerAddress, telephone, destination,
                carIdStr, scheduledDate, scheduledTime, fareType,
                "5", "10", customerEmail, "D12345678");

        // Assert that the booking creation failed due to the past date
        assertEquals("invalid_date", result,
                "Expected 'invalid_date' error but got: " + result);
    }
    @Test
    public void testScheduledTimeValidation() {
        // Test Case 3: Scheduled Time Validation
        // Test data with a scheduled time in the past
        String customerName = "John Doe";
        String customerEmail = "johndoe@example.com";
        String customerAddress = "789 Maple St";
        String telephone = "0987654321";
        String destination = "123 Pine Ave";
        String scheduledDate = "2025-03-15";  // Future date
        String scheduledTime = "10:00";  // Past time
        String carIdStr = "2";
        String fareType = "Luxury";
        String taxRateStr = "5";
        String discountRateStr = "10";
        String customerRegNumber = "D12345678";
        // Call the addBooking method to create the booking
        String result = bookingService.addBooking(customerName, customerAddress, telephone, destination,
                carIdStr, scheduledDate, scheduledTime, fareType,
                taxRateStr, discountRateStr, customerEmail, customerRegNumber);

        // Assert that the result contains "invalid_time" to indicate the scheduled time is invalid
        assertEquals("invalid_time", result, "Expected 'invalid_time' but got: " + result);
    }
    @Test
    public void testMissingFieldsValidation() {
        // Test Case 5: Missing Fields Validation
        // Test data with missing fields
        String customerName = "";
        String customerEmail = "onithjavon12@gmail.com";
        String customerAddress = "789 Maple St";
        String telephone = "0987654321";
        String destination = "";
        String scheduledDate = "2025-03-15";
        String scheduledTime = "10:30";
        String carIdStr = "2";
        String fareType = "Luxury";

        // Call the addBooking method to create the booking
        String result = bookingService.addBooking(customerName, customerAddress, telephone, destination,
                carIdStr, scheduledDate, scheduledTime, fareType,
                "5", "10", customerEmail, "D12345678");

        // Assert that the result indicates missing fields
        assertTrue(result.equals("missing_fields"),
                "Expected validation error for missing fields but got: " + result);
    }
    @Test
    public void testEditBooking() {
        // Test Case: Edit Booking with new customer email

        // Test data with updated customer name and email
        String customerName = "Eliana(Changed)";
        String customerEmail = "changed@gmail.com";  // Updated email
        String customerAddress = "123 Main St";
        String telephone = "0563119207";
        String destination = "Marina Walk";
        String scheduledDate = "2025-02-24";
        String scheduledTime = "16:00";
        String carIdStr = "2";
        String fareType = "Standard";

        // Call the editBooking method to update the booking
        String result = bookingService.updateBooking("ORD-57577", customerName, customerAddress, telephone,
                destination, scheduledDate, scheduledTime, fareType, 15.0, 12.0, 10.0, 10.0, customerEmail);

        // Assert that the booking details are updated successfully
        assertEquals("success", result,
                "Expected booking to be successfully updated but got: " + result);
    }

    @Test
    public void testCancelBooking() {
        // Test Case: Cancel Booking

        // Test data with a valid booking ID
        String orderNumber = "ORD-76754";

        // Employee confirming the cancellation
        String confirmedByEmployee = "D12345678";

        // Call the cancelBooking method to cancel the booking
        String result = bookingService.cancelBooking(orderNumber, confirmedByEmployee);

        //booking is successfully canceled
        assertEquals("success", result,
                "Expected booking to be canceled but got: " + result);

        //  status of the booking is updated to 'Cancelled'
        Booking updatedBooking = bookingDAO.getBookingByOrderNumber(orderNumber);
        assertEquals("Cancelled", updatedBooking.getStatus(),
                "Expected booking status to be 'Cancelled' but got: " + updatedBooking.getStatus());
    }
    @Test
    public void testDeleteBooking() {
        // Test Case: Delete Booking

        // Test data with a valid booking ID
        String orderNumber = "ORD-76754";

        // Call the deleteBooking method to delete the booking
        String result = bookingService.deleteBooking(orderNumber);

        //  booking is successfully deleted
        assertEquals("success", result,
                "Expected booking to be deleted but got: " + result);

        //  booking is no longer in the system
        Booking deletedBooking = bookingDAO.getBookingByOrderNumber(orderNumber);
        assertNull(deletedBooking, "Expected booking to be deleted, but it still exists.");
    }
    @Test
    public void testMarkAsCompleted() {
        // Test Case: Mark As Completed Booking

        // Test data with a valid booking ID
        String orderNumber = "ORD-78005";

        // Call the completeBooking method in the BookingService to mark the booking as completed
        String result = bookingService.completeBooking(orderNumber);

        //  booking is successfully marked as completed
        assertEquals("success", result,
                "Expected booking to be marked as completed but got: " + result);

        //status of the booking is updated to 'Completed'
        Booking updatedBooking = bookingDAO.getBookingByOrderNumber(orderNumber);
        assertEquals("Completed", updatedBooking.getStatus(),
                "Expected booking status to be 'Completed' but got: " + updatedBooking.getStatus());
    }


}
