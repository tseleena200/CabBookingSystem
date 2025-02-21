package com.example.cabbookingsystem.model;

import com.example.cabbookingsystem.util.FareContext;
import com.example.cabbookingsystem.util.FareStrategy;
import com.example.cabbookingsystem.util.LuxuryFare;
import com.example.cabbookingsystem.util.SharedFare;
import com.example.cabbookingsystem.util.StandardFare;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

public class Booking {
    private static final Logger LOGGER = Logger.getLogger(Booking.class.getName());

    private int bookingId;
    private String orderNumber;
    private String customerName;
    private String customerAddress;
    private String phoneNumber;
    private String destination;
    private Date bookingDate;
    private Date scheduledDate;
    private String scheduledTime;
    private String status;
    private double totalAmount;
    private int carId;
    private int driverId;
    private String customerRegistrationNumber; //It is Employee number
    private String confirmedByEmployee;
    private String fareType;
    private double baseFare;
    private double distance;
    private double taxRate;
    private double discountRate;
    private String customerEmail;
    private String model;  //  Car Model
    private String licensePlate;  //  Car License Plate
    private String fullName; ;  //  Driver Name



    //  Default Constructor
    public Booking() {
        LOGGER.log(Level.INFO, "📌 Booking object created (empty constructor)");
    }

    //  Constructor with all fields
    public Booking(String orderNumber, String customerName, String customerAddress, String phoneNumber,
                   String destination, Date bookingDate, Date scheduledDate, String scheduledTime,
                   int carId, int driverId, String customerRegistrationNumber, String confirmedByEmployee,
                   String fareType, double baseFare, double distance, double taxRate, double discountRate,
                   String status, String customerEmail, String model, String licensePlate, String fullName) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.phoneNumber = phoneNumber;
        this.destination = destination;
        this.bookingDate = bookingDate;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.carId = carId;
        this.driverId = driverId;
        this.customerRegistrationNumber = customerRegistrationNumber;
        this.confirmedByEmployee = confirmedByEmployee;
        this.fareType = (fareType != null && !fareType.trim().isEmpty()) ? fareType : "Standard";
        this.baseFare = baseFare;
        this.distance = distance;
        this.taxRate = taxRate;
        this.discountRate = discountRate;
        this.status = (status != null && !status.trim().isEmpty()) ? status : "Pending";
        this.customerEmail = customerEmail;
        this.model = model;
        this.licensePlate = licensePlate;
        this.fullName = fullName;

        LOGGER.log(Level.INFO, "✅ Booking Created - Order#: {0}, Customer: {1}, Model: {2}",
                new Object[]{orderNumber, customerName, model});
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public java.sql.Date getScheduledDate() { return new java.sql.Date(scheduledDate.getTime()); }
    public void setScheduledDate(Date scheduledDate) { this.scheduledDate = new java.sql.Date(scheduledDate.getTime()); }

    public String getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalAmount() {
        return (totalAmount > 0) ? totalAmount : calculateFare();  // ✅ Auto-calculate if needed
    }

    public void setTotalAmount(double totalAmount) {
        LOGGER.log(Level.INFO, "📌 Setting Total Amount - Previous: {0}, New: {1}, Order#: {2}",
                new Object[]{this.totalAmount, totalAmount, orderNumber});
        this.totalAmount = totalAmount;
    }

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public String getCustomerRegistrationNumber() { return customerRegistrationNumber; }
    public void setCustomerRegistrationNumber(String customerRegistrationNumber) { this.customerRegistrationNumber = customerRegistrationNumber; }

    public String getConfirmedByEmployee() { return confirmedByEmployee; }
    public void setConfirmedByEmployee(String confirmedByEmployee) { this.confirmedByEmployee = confirmedByEmployee; }

    public String getFareType() { return fareType; }
    public void setFareType(String fareType) {
        this.fareType = (fareType != null && !fareType.trim().isEmpty()) ? fareType : "Standard";
    }

    public double getBaseFare() { return baseFare; }
    public void setBaseFare(double baseFare) { this.baseFare = baseFare; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public double getTaxRate() { return taxRate; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }

    public double getDiscountRate() { return discountRate; }
    public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }

    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    //  Getter & Setter for fullName
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public double calculateFare() {
        return calculateFare(this.baseFare, this.distance, this.taxRate, this.discountRate);
    }

    public double calculateFare(double baseFare, double distance, double taxRate, double discountRate) {
        LOGGER.log(Level.INFO, "📌 Calculating Fare - Base: {0}, Distance: {1}, FareType: {2}, Order#: {3}, Tax: {4}%, Discount: {5}%",
                new Object[]{baseFare, distance, fareType, orderNumber, taxRate, discountRate});

        if (fareType == null) {
            fareType = "Standard";
        }

        FareStrategy fareStrategy;
        switch (fareType) {
            case "Luxury":
                fareStrategy = new LuxuryFare();
                break;
            case "Shared":
                fareStrategy = new SharedFare();
                break;
            default:
                fareStrategy = new StandardFare();
        }

        FareContext fareContext = new FareContext(fareStrategy);
        double calculatedFare = fareContext.executeStrategy(baseFare, distance, taxRate, discountRate);

        LOGGER.log(Level.INFO, "✅ Fare Calculated: {0} (Base: {1}, Distance: {2}, FareType: {3}, Tax: {4}%, Discount: {5}%)",
                new Object[]{calculatedFare, baseFare, distance, fareType, taxRate, discountRate});

        return calculatedFare;
    }
}
