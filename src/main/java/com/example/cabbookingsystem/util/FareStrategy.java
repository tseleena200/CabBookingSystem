package com.example.cabbookingsystem.util;

public interface FareStrategy {
    double calculateFare(double baseFare, double distance, double taxRate, double discountRate);
}
