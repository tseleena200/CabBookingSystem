package com.example.cabbookingsystem.util;

import com.example.cabbookingsystem.util.FareStrategy;

public class LuxuryFare implements FareStrategy {
    @Override
    public double calculateFare(double baseFare, double distance, double taxRate, double discountRate) {
        double totalFare = (baseFare * 1.5) * distance; // 1.5x multiplier for luxury
        double taxAmount = (totalFare * taxRate) / 100;
        double discountAmount = (totalFare * discountRate) / 100;
        totalFare = totalFare + taxAmount - discountAmount;
        return totalFare;
    }
}
