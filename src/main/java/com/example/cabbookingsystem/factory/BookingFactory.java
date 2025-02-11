package com.example.cabbookingsystem.factory;

import com.example.cabbookingsystem.model.Booking;
import java.util.Date;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BookingFactory {
    private static final Logger LOGGER = Logger.getLogger(BookingFactory.class.getName());

    public static Booking createBooking(String orderNumber, String customerName, String customerAddress, String phoneNumber,
                                        String destination, Date bookingDate, Date scheduledDate, String scheduledTime,
                                        int carId, int driverId, String customerRegistrationNumber, String confirmedBy,
                                        String fareType, double baseFare, double distance, double taxRate, double discountRate, String status, String customerEmail) {


        fareType = (fareType != null) ? fareType : "Standard";


        taxRate = (taxRate >= 0) ? taxRate : 0.0;
        discountRate = (discountRate >= 0) ? discountRate : 0.0;


        LOGGER.log(Level.INFO, " DEBUG: BookingFactory Received Email = {0}", customerEmail);


        return new Booking(orderNumber, customerName, customerAddress, phoneNumber, destination,
                bookingDate, scheduledDate, scheduledTime, carId, driverId, customerRegistrationNumber, confirmedBy,
                fareType, baseFare, distance, taxRate, discountRate, status, customerEmail);
    }
}

