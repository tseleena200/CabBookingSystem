package com.example.cabbookingsystem.util;

import com.example.cabbookingsystem.util.FareStrategy;

public class StandardFare implements FareStrategy {
    @Override
    public double calculateFare(double baseFare, double distance, double taxRate, double discountRate) {
        double totalFare = baseFare * distance;
        double taxAmount = (totalFare * taxRate) / 100;
        double discountAmount = (totalFare * discountRate) / 100;
        totalFare = totalFare + taxAmount - discountAmount;
        return totalFare;
    }
}
