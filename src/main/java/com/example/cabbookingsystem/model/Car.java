package com.example.cabbookingsystem.model;

public class Car {
    private int id;
    private String model;
    private String licensePlate;
    private int capacity;
    private Integer driverId; // Can be NULL

    // Constructor for adding a new car
    public Car(String model, String licensePlate, int capacity, Integer driverId) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.driverId = driverId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }
}