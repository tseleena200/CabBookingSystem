package com.example.cabbookingsystem.dao;

import com.example.cabbookingsystem.model.Car;
import com.example.cabbookingsystem.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CarDAO {
    private static CarDAO instance;
    private final Connection connection;

    private CarDAO() {
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    public static CarDAO getInstance() {
        if (instance == null) {
            synchronized (CarDAO.class) {
                if (instance == null) {
                    instance = new CarDAO();
                }
            }
        }
        return instance;
    }

    public boolean addCar(Car car) {
        String query = "INSERT INTO cars (model, license_plate, capacity, driver_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, car.getModel());
            stmt.setString(2, car.getLicensePlate());
            stmt.setInt(3, car.getCapacity());
            stmt.setInt(4, car.getDriverId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" [ERROR] Failed to add car: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
