package com.example.cabbookingsystem.service;

import com.example.cabbookingsystem.dao.BookingDAO;
import com.example.cabbookingsystem.dao.CarDAO;
import com.example.cabbookingsystem.dao.DriverDAO;
import com.example.cabbookingsystem.model.Booking;
import com.example.cabbookingsystem.factory.BookingFactory;
import com.example.cabbookingsystem.util.*;

import java.sql.Timestamp;
import java.util.Date;

public class BookingService {
    private final BookingDAO bookingDAO = BookingDAO.getInstance();
    private final CarDAO carDAO = CarDAO.getInstance();
    private final DriverDAO driverDAO = DriverDAO.getInstance();


    // Add booking logic
    public String addBooking(String customerName, String customerAddress, String phoneNumber, String destination,
                             String carIdStr, String scheduledDateStr, String scheduledTimeStr, String fareType,
                             String taxRateStr, String discountRateStr, String customerEmail, String customerRegNumber) {

        // Validate required fields
        if (customerName == null || customerAddress == null || phoneNumber == null || destination == null ||
                carIdStr == null || scheduledDateStr == null || scheduledTimeStr == null || fareType == null ||
                customerEmail == null || customerName.isEmpty() || customerAddress.isEmpty() || phoneNumber.isEmpty() ||
                destination.isEmpty() || carIdStr.isEmpty() || scheduledDateStr.isEmpty() || scheduledTimeStr.isEmpty() ||
                fareType.isEmpty() || customerEmail.isEmpty()) {
            return "missing_fields";
        }

        // Parsing the car ID and scheduled date
        int carId = Integer.parseInt(carIdStr);
        Date scheduledDate;
        try {
            scheduledDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(scheduledDateStr);
        } catch (Exception e) {
            return "invalid_date";
        }

        // Validate if the car is available
        int driverId = bookingDAO.getDriverIdByCarId(carId);
        if (driverId == -1) {
            return "driver_not_found";
        }

        if (bookingDAO.isCarBooked(carId, new java.sql.Date(scheduledDate.getTime()), scheduledTimeStr)) {
            return "car_already_booked";
        }

        // Generate unique order number
        String orderNumber = "ORD-" + System.currentTimeMillis();

        // Default values for base fare & distance
        double baseFare = 10.0;
        double distance = 5.0;

        // Parse tax and discount rates
        double taxRate = (taxRateStr != null && !taxRateStr.isEmpty()) ? Double.parseDouble(taxRateStr) : 5.0;
        double discountRate = (discountRateStr != null && !discountRateStr.isEmpty()) ? Double.parseDouble(discountRateStr) : 10.0;

        // Fetch car model and license plate, and driver name
        String model = carDAO.getCarModelById(carId);
        String licensePlate = carDAO.getCarLicensePlateById(carId);
        String fullName = driverDAO.getDriverFullNameById(driverId);

        // Create a new booking
        Booking newBooking = BookingFactory.createBooking(
                orderNumber, customerName, customerAddress, phoneNumber, destination,
                new Timestamp(System.currentTimeMillis()), scheduledDate, scheduledTimeStr,
                carId, driverId, customerRegNumber, null,
                fareType, baseFare, distance, taxRate, discountRate, "Pending", customerEmail,
                model, licensePlate, fullName
        );

        // Calculate the total fare
        double totalFare = newBooking.calculateFare();
        newBooking.setTotalAmount(totalFare);

        // Add the booking to the database
        boolean success = bookingDAO.addBooking(newBooking);

        if (success) {
            return "success:" + newBooking.getOrderNumber();
        } else {
            return "db_insert_fail";
        }
    }

    // Method to confirm a booking
    public String confirmBooking(String orderNumber, String employeeRegNum) {
        // Check current status of booking
        String currentStatus = bookingDAO.getBookingStatus(orderNumber);

        if (currentStatus == null) {
            return "booking_not_found";  // Booking not found
        }

        if ("Confirmed".equalsIgnoreCase(currentStatus)) {
            return "already_confirmed";  // Booking is already confirmed
        }

        // Update the booking status to "Confirmed"
        boolean success = bookingDAO.updateBookingStatus(orderNumber, "Confirmed", employeeRegNum);
        return success ? "success" : "update_failed";  // Return success or failure
    }
    // Method to cancel a booking
    public String cancelBooking(String orderNumber, String confirmedByEmployee) {
        // Check if booking exists
        String currentStatus = bookingDAO.getBookingStatus(orderNumber);

        if (currentStatus == null) {
            return "booking_not_found";  // Booking does not exist
        }

        if ("Cancelled".equalsIgnoreCase(currentStatus)) {
            return "already_cancelled";  // Booking is already cancelled
        }

        // Update booking status to "Cancelled"
        boolean success = bookingDAO.updateBookingStatus(orderNumber, "Cancelled", confirmedByEmployee);
        return success ? "success" : "update_failed";  // Return success or failure
    }
    // Method to complete a booking
    public String completeBooking(String orderNumber) {
        // Check if the booking exists and its status
        String currentStatus = bookingDAO.getBookingStatus(orderNumber);

        if (currentStatus == null) {
            return "booking_not_found";  // Booking does not exist
        }

        if ("Completed".equalsIgnoreCase(currentStatus)) {
            return "already_completed";  // Booking is already completed
        }

        // Update booking status to "Completed"
        boolean success = bookingDAO.completeBooking(orderNumber);
        return success ? "success" : "update_failed";  // Return success or failure
    }

    // Method to delete a booking
    public String deleteBooking(String orderNumber) {
        if (orderNumber == null || orderNumber.isEmpty()) {
            return "missing_order_number";  // Ensure order number is not null or empty
        }

        boolean success = bookingDAO.deleteBooking(orderNumber);
        return success ? "success" : "delete_failed";  // Return success or failure
    }
    // Method to handle the update booking logic
    public String updateBooking(String orderNumber, String customerName, String address, String telephone, String destination,
                                String scheduledDate, String scheduledTime, String fareType, double baseFare,
                                double distance, double taxRate, double discountRate, String customerEmail) {

        // Fetch the existing booking by order number
        Booking booking = bookingDAO.getBookingByOrderNumber(orderNumber);

        if (booking == null) {
            return "error_booking_not_found"; // If booking doesn't exist, return error
        }

        // Update the booking with new details
        booking.setCustomerName(customerName);
        booking.setCustomerAddress(address);
        booking.setPhoneNumber(telephone);
        booking.setDestination(destination);
        booking.setScheduledDate(java.sql.Date.valueOf(scheduledDate));  // Assuming the date is passed in "yyyy-MM-dd" format
        booking.setScheduledTime(scheduledTime);
        booking.setFareType(fareType);
        booking.setCustomerEmail(customerEmail);
        booking.setBaseFare(baseFare);
        booking.setDistance(distance);
        booking.setTaxRate(taxRate);
        booking.setDiscountRate(discountRate);

        // Recalculate the total amount after the changes
        double totalAmount = booking.calculateFare();
        booking.setTotalAmount(totalAmount);

        // Call the update method in DAO to save the changes
        boolean success = bookingDAO.updateBookingDetails(orderNumber, baseFare, distance, taxRate, discountRate, totalAmount);

        return success ? "success" : "error_db_update"; // Return success or database failure message
    }

    // Method to calculate the bill
    public String calculateBill(String orderNumber, double distance, double baseFare, double taxRate, double discountRate) {
        // Fetch the booking by order number
        Booking booking = bookingDAO.getBookingByOrderNumber(orderNumber);

        if (booking == null) {
            return "booking_not_found";  // Booking doesn't exist
        }

        // Ensure booking is confirmed before calculating the bill
        if (!"Confirmed".equals(booking.getStatus())) {
            return "not_confirmed";  // Booking not confirmed yet
        }

        // Set the updated details in the booking object
        booking.setBaseFare(baseFare);
        booking.setDistance(distance);
        booking.setTaxRate(taxRate);
        booking.setDiscountRate(discountRate);

        // Calculate the total fare using the strategy pattern (using the FareContext and FareStrategy)
        FareStrategy fareStrategy;
        switch (booking.getFareType()) {
            case "Luxury":
                fareStrategy = new LuxuryFare();  // Luxury Fare Strategy
                break;
            case "Shared":
                fareStrategy = new SharedFare();  // Shared Fare Strategy
                break;
            default:
                fareStrategy = new StandardFare();  // Standard Fare Strategy
        }

        FareContext fareContext = new FareContext(fareStrategy);
        double totalFare = fareContext.executeStrategy(baseFare, distance, taxRate, discountRate);  // Fare calculation logic

        // Set the calculated total fare
        booking.setTotalAmount(totalFare);

        // Update the booking details in the database
        boolean updated = bookingDAO.updateBookingDetails(orderNumber, baseFare, distance, taxRate, discountRate, totalFare);

        return updated ? "success:" + String.format("%.2f", totalFare) : "update_failed";  // Return success or failure
    }


}
