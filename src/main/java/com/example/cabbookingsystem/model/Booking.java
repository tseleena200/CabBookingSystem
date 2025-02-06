package com.example.cabbookingsystem.model;

import java.util.Date;

public class Booking {
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
    private String customerRegistrationNumber;
    private String confirmedByEmployee;

    //  Default Constructor
    public Booking() {}

    //  Constructor (With Order Number)
    public Booking(String orderNumber, String customerName, String customerAddress, String phoneNumber, String destination,
                   Date bookingDate, Date scheduledDate, String scheduledTime, int carId, int driverId,
                   String customerRegistrationNumber, String confirmedByEmployee) {
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
        this.totalAmount = 0.0;
        this.status = "Pending";
        this.confirmedByEmployee = confirmedByEmployee;
    }

    //  Getters and Setters
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
    public void setScheduledDate(Date scheduledDate) { this.scheduledDate = scheduledDate; }

    public String getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public String getCustomerRegistrationNumber() { return customerRegistrationNumber; }
    public void setCustomerRegistrationNumber(String customerRegistrationNumber) { this.customerRegistrationNumber = customerRegistrationNumber; }

    public String getConfirmedByEmployee() { return confirmedByEmployee; }
    public void setConfirmedByEmployee(String confirmedByEmployee) { this.confirmedByEmployee = confirmedByEmployee; }

    //  Helper method to update status safely
    public void updateStatus(String newStatus) {
        if (newStatus.equals("Pending") || newStatus.equals("Confirmed") || newStatus.equals("Completed") || newStatus.equals("Cancelled")) {
            this.status = newStatus;
        }
    }
}
