package com.example.cabbookingsystem.factory;

import com.example.cabbookingsystem.model.Booking;
import java.util.Date;

public class BookingFactory {
    public static Booking createBooking(String orderNumber, String customerName, String customerAddress, String phoneNumber,
                                        String destination, Date bookingDate, Date scheduledDate, String scheduledTime,
                                        int carId, int driverId, String customerRegistrationNumber, String confirmedBy) {

        //  Create a booking object with all required parameters
        Booking booking = new Booking(orderNumber, customerName, customerAddress, phoneNumber, destination,
                bookingDate, scheduledDate, scheduledTime, carId, driverId, customerRegistrationNumber, confirmedBy);

        //  Set default status as "Pending"
        booking.setStatus("Pending");

        return booking;
    }
}
