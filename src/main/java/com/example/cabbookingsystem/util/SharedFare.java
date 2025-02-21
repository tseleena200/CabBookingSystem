package com.example.cabbookingsystem.util;

import com.example.cabbookingsystem.util.FareStrategy;

public class SharedFare implements FareStrategy {
    @Override
    public double calculateFare(double baseFare, double distance, double taxRate, double discountRate) {
        double totalFare = (baseFare * 0.8) * distance; // 0.8x multiplier for shared
        double taxAmount = (totalFare * taxRate) / 100;
        double discountAmount = (totalFare * discountRate) / 100;
        totalFare = totalFare + taxAmount - discountAmount;
        return totalFare;
    }
}
