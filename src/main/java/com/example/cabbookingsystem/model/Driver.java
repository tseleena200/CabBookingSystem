package com.example.cabbookingsystem.model;

public class Driver {
    private int id;
    private String fullName;
    private String licenseNumber;
    private String contactNumber;

    //  Default constructor
    public Driver() {}

    //  Constructor for adding a new driver
    public Driver(String fullName, String licenseNumber, String contactNumber) {
        this.fullName = fullName;
        this.licenseNumber = licenseNumber;
        this.contactNumber = contactNumber;
    }

    // Constructor for fetching drivers (only id and name)
    public Driver(int id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    //  Full constructor
    public Driver(int id, String fullName, String licenseNumber, String contactNumber) {
        this.id = id;
        this.fullName = fullName;
        this.licenseNumber = licenseNumber;
        this.contactNumber = contactNumber;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}
