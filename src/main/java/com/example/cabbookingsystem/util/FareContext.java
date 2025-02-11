package com.example.cabbookingsystem.util;

public class FareContext {
    private FareStrategy fareStrategy;


    public FareContext(FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }


    public double executeStrategy(double baseFare, double distance, double taxRate, double discountRate) {
        return fareStrategy.calculateFare(baseFare, distance, taxRate, discountRate);
    }
}
