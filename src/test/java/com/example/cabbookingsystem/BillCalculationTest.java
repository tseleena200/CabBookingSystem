package com.example.cabbookingsystem;

import com.example.cabbookingsystem.dao.BookingDAO;
import com.example.cabbookingsystem.model.Booking;
import com.example.cabbookingsystem.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.*;

public class BillCalculationTest {

    private final BookingService bookingService = new BookingService();
    private final BookingDAO bookingDAO = BookingDAO.getInstance();  // Instance of BookingDAO

    @BeforeEach
    public void setUp() {
        // Set up initial conditions if needed, e.g., initializing mock data
    }

    @Test
    public void testBillCalculation() {
        // Test Case 1: Valid Bill Calculation
        // Test data for valid booking
        String orderNumber = "ORD-90263";
        double distance = 15.0;
        double baseFare = 10.0;
        double taxRate = 15.0;
        double discountRate = 10.0;
        // Expected result based on standard fare calculation
        double expectedFare = 157.50;  // Manually calculated expected fare

        // Call the calculateBill method to get the calculated fare
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);

        assertTrue(result.startsWith("success"),
                "Expected booking bill calculation to be successful but got: " + result);

        // Retrieve the updated booking to check the total amount
        Booking updatedBooking = bookingDAO.getBookingByOrderNumber(orderNumber);

        // Ensure the fare is correct
        assertEquals(expectedFare, updatedBooking.getTotalAmount(),
                "Expected total fare to be " + expectedFare + " but got: " + updatedBooking.getTotalAmount());

        // Check if the result contains the expected success message along with the fare
        assertTrue(result.contains(String.format("%.2f", expectedFare)),
                "Expected result to contain success and calculated fare: " + expectedFare);
    }

    @Test
    public void testInvalidOrderNumber() {
        // Test Case 2: Validate system behavior with an invalid order number
        // Test data with an invalid order number
        String invalidOrderNumber = "ORD-99999";  // An order number that doesn't exist
        double distance = 15.0;
        double baseFare = 10.0;
        double taxRate = 15.0;
        double discountRate = 10.0;

        // Call the calculateBill method to attempt bill calculation with an invalid order number
        String result = bookingService.calculateBill(invalidOrderNumber, distance, baseFare, taxRate, discountRate);

        //  result is "booking_not_found"
        assertEquals("booking_not_found", result,
                "Expected 'booking_not_found' error but got: " + result);
    }
    @Test
    public void testBillCalculationNotConfirmed() {
        // Test Case: Bill Calculation when booking is not confirmed
        // Test data for a booking that is not confirmed
        String orderNumber = "ORD-1096"; // booking is in 'Pending' status
        double distance = 15.0;
        double baseFare = 10.0;
        double taxRate = 15.0;
        double discountRate = 10.0;

        // Expected result is 'not_confirmed' as the booking is not yet confirmed
        String expectedResult = "not_confirmed";

        // Call the calculateBill method to try to calculate the fare
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);
        assertEquals(expectedResult, result, "Expected booking bill calculation to fail with 'not_confirmed' but got: " + result);
    }

    @Test
    public void testBillCalculationAlreadyCalculated() {
        // Test Case 3: Bill Already Calculated

        // Test data for a booking where the bill has already been calculated
        String orderNumber = "ORD-12345";  // order number has a calculated bill
        double distance = 15.0;
        double baseFare = 10.0;
        double taxRate = 15.0;
        double discountRate = 10.0;

        // Call the calculateBill method to attempt bill calculation for an already calculated booking
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);

        // Assert that the result is "already_calculated"
        assertEquals("already_calculated", result,
                "Expected 'already_calculated' error but got: " + result);
    }

    @Test
    public void testFareCalculationTwice() {
        // Test Case 4 : Ensure that the fare cannot be calculated twice for the same booking
        // Test data for a booking where the bill has already been calculated
        String orderNumber = "ORD-90263"; //  bill has already been calculated
        double distance = 15.0;
        double baseFare = 10.0;
        double taxRate = 15.0;
        double discountRate = 10.0;

        // Expected result is 'already_calculated' as the bill has already been calculated
        String expectedResult = "already_calculated";

        // Call the calculateBill method for the same booking again
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);

        // Assert that the result matches the expected outcome
        assertEquals(expectedResult, result, "Expected booking bill calculation to fail with 'already_calculated' but got: " + result);
    }


    @Test
    public void testStandardFareCalculation() {
        // Test Case: Standard Fare Calculation
        String orderNumber = "ORD-36544"; // Standard fare order number
        double distance = 15.0;
        double baseFare = 10.0;
        double taxRate = 15.0;
        double discountRate = 10.0;

        // Expected result for Standard Fare calculation
        double expectedFare = 157.5;  // (Base Fare * Distance) + Tax - Discount

        // Call the calculateBill method for Standard fare
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);
        DecimalFormat df = new DecimalFormat("0.0");
        String expectedFormatted = "success:" + df.format(expectedFare);
        String resultFormatted = result.trim();
        resultFormatted = resultFormatted.length() > 0 ? resultFormatted : null;
        resultFormatted = resultFormatted != null ? resultFormatted.split(":")[0] + ":" + df.format(Double.parseDouble(resultFormatted.split(":")[1])) : resultFormatted;

        // Assert that the result contains the expected fare
        assertEquals(expectedFormatted, resultFormatted,
                "Expected booking bill calculation to be successful but got: " + resultFormatted);

        // Retrieve the updated booking to check the total amount
        Booking updatedBooking = bookingDAO.getBookingByOrderNumber(orderNumber);

        assertEquals(expectedFare, updatedBooking.getTotalAmount(), 0.01,
                "Expected total fare to be " + expectedFare + " but got: " + updatedBooking.getTotalAmount());
    }


    @Test
    public void testLuxuryFareCalculation() {
        // Test Case: Luxury Fare Calculation
        String orderNumber = "ORD-28434"; // Luxury fare order number
        double distance = 15.0;
        double baseFare = 10.0;
        double taxRate = 15.0;
        double discountRate = 10.0;

        // Expected result for Luxury Fare calculation
        double expectedFare = 236.25;  // (Base Fare * Distance * 1.5) + Tax - Discount

        // Call the calculateBill method for Luxury fare
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);
        DecimalFormat df = new DecimalFormat("0.0");
        String expectedFormatted = "success:" + df.format(expectedFare);
        String resultFormatted = result.trim();
        resultFormatted = resultFormatted.length() > 0 ? resultFormatted : null;
        resultFormatted = resultFormatted != null ? resultFormatted.split(":")[0] + ":" + df.format(Double.parseDouble(resultFormatted.split(":")[1])) : resultFormatted;

        assertEquals(expectedFormatted, resultFormatted,
                "Expected booking bill calculation to be successful but got: " + resultFormatted);

        // Retrieve the updated booking to check the total amount
        Booking updatedBooking = bookingDAO.getBookingByOrderNumber(orderNumber);

        assertEquals(expectedFare, updatedBooking.getTotalAmount(), 0.01,
                "Expected total fare to be " + expectedFare + " but got: " + updatedBooking.getTotalAmount());
    }

    @Test
    public void testSharedFareCalculation() {
        // Test Case: Shared Fare Calculation
        String orderNumber = "ORD-96490"; // Shared fare order number
        double distance = 15.0;
        double baseFare = 10.0;
        double taxRate = 15.0;
        double discountRate = 10.0;

        // Expected result for Shared Fare calculation
        double expectedFare = 126.0;  // (Base Fare * Distance * 0.8) + Tax - Discount

        // Call the calculateBill method for Shared fare
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);
        DecimalFormat df = new DecimalFormat("0.0");
        String expectedFormatted = "success:" + df.format(expectedFare);

        String resultFormatted = result.trim();
        resultFormatted = resultFormatted.length() > 0 ? resultFormatted : null;
        resultFormatted = resultFormatted != null ? resultFormatted.split(":")[0] + ":" + df.format(Double.parseDouble(resultFormatted.split(":")[1])) : resultFormatted;
        assertEquals(expectedFormatted, resultFormatted,
                "Expected booking bill calculation to be successful but got: " + resultFormatted);

        // Retrieve the updated booking to check the total amount
        Booking updatedBooking = bookingDAO.getBookingByOrderNumber(orderNumber);
        assertEquals(expectedFare, updatedBooking.getTotalAmount(), 0.01,
                "Expected total fare to be " + expectedFare + " but got: " + updatedBooking.getTotalAmount());
    }
    @Test
    public void testInvalidDistance() {
        // Test Case: Invalid Distance
        String orderNumber = "ORD-79468"; // Invalid order number
        double distance = -10.0;  // Invalid distance
        double baseFare = 10.0;
        double taxRate = 5.0;
        double discountRate = 10.0;

        // Expected error message
        String expectedError = "Error: Please enter a valid distance greater than 0.";

        // Call the calculateBill method for invalid distance
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);

        // Assert the expected error message
        assertEquals(expectedError, result,
                "Expected error message but got: " + result);
    }

    @Test
    public void testInvalidBaseFare() {
        // Test Case: Invalid Base Fare
        String orderNumber = "ORD-79468";
        double distance = 10.0;
        double baseFare = 0.0;  // Invalid base fare (zero)
        double taxRate = 5.0;
        double discountRate = 10.0;

        // Expected error message
        String expectedError = "Error: Please enter a valid base fare greater than 0.";

        // Call the calculateBill method for invalid base fare
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);

        // Assert the expected error message
        assertEquals(expectedError, result,
                "Expected error message but got: " + result);
    }

    @Test
    public void testInvalidTaxRate() {
        // Test Case: Invalid Tax Rate
        String orderNumber = "ORD-79468";
        double distance = 10.0;
        double baseFare = 10.0;
        double taxRate = -20.0;  // Invalid tax rate (negative)
        double discountRate = 10.0;

        // Expected error message
        String expectedError = "Error: Please enter a valid tax rate (0-100%).";

        // Call the calculateBill method for invalid tax rate
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);

        // Assert the expected error message
        assertEquals(expectedError, result,
                "Expected error message but got: " + result);
    }

    @Test
    public void testInvalidDiscountRate() {
        // Test Case: Invalid Discount Rate
        String orderNumber = "ORD-79468";
        double distance = 10.0;
        double baseFare = 10.0;
        double taxRate = 5.0;
        double discountRate = -5.0;  // Invalid discount rate (negative)

        // Expected error message
        String expectedError = "Error: Please enter a valid discount rate (0-100%).";

        // Call the calculateBill method for invalid discount rate
        String result = bookingService.calculateBill(orderNumber, distance, baseFare, taxRate, discountRate);

        // Assert the expected error message
        assertEquals(expectedError, result,
                "Expected error message but got: " + result);
    }


}
