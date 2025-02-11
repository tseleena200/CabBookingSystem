package com.example.cabbookingsystem.util;

public class SharedFare implements FareStrategy {
    @Override
    public double calculateFare(double baseFare, double distance, double taxRate, double discountRate) {
        double distanceRate = 1.8;
        double timeRate = 0.3;
        double fare = baseFare + (distance * distanceRate) + (distance * timeRate);


        fare += (fare * (taxRate / 100));
        fare -= (fare * (discountRate / 100));
        return fare;
    }
}
