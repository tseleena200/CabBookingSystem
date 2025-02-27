package com.example.cabbookingsystem.dao;

import com.example.cabbookingsystem.model.Car;
import com.example.cabbookingsystem.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    //  Add a new car
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

    //  Fetch all cars (for manage.jsp)
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT c.id, c.model, c.license_plate, c.capacity, c.driver_id, d.full_name AS driver_name " +
                "FROM cars c LEFT JOIN drivers d ON c.driver_id = d.id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Car car = new Car();
                car.setId(rs.getInt("id"));
                car.setModel(rs.getString("model"));
                car.setLicensePlate(rs.getString("license_plate"));
                car.setCapacity(rs.getInt("capacity"));
                car.setDriverId(rs.getInt("driver_id"));
                car.setDriverName(rs.getString("driver_name"));

                cars.add(car);
            }

        } catch (SQLException e) {
            System.out.println("🚨 [ERROR] Failed to fetch cars: " + e.getMessage());
            e.printStackTrace();
        }
        return cars;
    }

    //  Fetch a specific car by ID
    public Car getCarById(int carId) {
        String query = "SELECT c.id, c.model, c.license_plate, c.capacity, c.driver_id, d.full_name AS driver_name " +
                "FROM cars c LEFT JOIN drivers d ON c.driver_id = d.id WHERE c.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Car car = new Car();
                car.setId(rs.getInt("id"));
                car.setModel(rs.getString("model"));
                car.setLicensePlate(rs.getString("license_plate"));
                car.setCapacity(rs.getInt("capacity"));
                car.setDriverId(rs.getInt("driver_id"));
                car.setDriverName(rs.getString("driver_name"));

                return car;
            }
        } catch (SQLException e) {
            System.out.println("🚨 [ERROR] Failed to fetch car: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //  Update car details
    public boolean updateCar(Car car) {
        String query = "UPDATE cars SET model = ?, license_plate = ?, capacity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, car.getModel());
            stmt.setString(2, car.getLicensePlate());
            stmt.setInt(3, car.getCapacity());
            stmt.setInt(4, car.getId());

            int rowsUpdated = stmt.executeUpdate();
            System.out.println(" Car Update Query Executed, Rows Affected: " + rowsUpdated);
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println(" [ERROR] Failed to update car: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean isDuplicateLicense(String licensePlate) {
        String query = "SELECT COUNT(*) FROM cars WHERE license_plate = ?"; // Exact match
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, licensePlate);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("🚨 [ERROR] Failed to check duplicate license: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // No duplicate found
    }

    // Check if a driver is already assigned to another car
    public boolean isDriverAssigned(int driverId) {
        String query = "SELECT COUNT(*) FROM cars WHERE driver_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0; // Returns true if driver is assigned
        } catch (SQLException e) {
            System.out.println(" [ERROR] Failed to check driver assignment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    //  Fetch Car Model by Car ID
    public String getCarModelById(int carId) {
        String model = "Unknown Model"; // Default if not found
        String query = "SELECT model FROM cars WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                model = rs.getString("model");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    //  Fetch Car's License Plate by Car ID
    public String getCarLicensePlateById(int carId) {
        String licensePlate = "Unknown Plate"; // Default if not found
        String query = "SELECT license_plate FROM cars WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                licensePlate = rs.getString("license_plate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return licensePlate;
    }

    //  Delete a car by ID
    public boolean deleteCar(int carId) {
        String query = "DELETE FROM cars WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, carId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" [ERROR] Failed to delete car: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public Car getCarByDriverId(int driverId) {
        String query = "SELECT id, model, license_plate, capacity, driver_id FROM cars WHERE driver_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Car car = new Car();
                car.setId(rs.getInt("id"));
                car.setModel(rs.getString("model"));
                car.setLicensePlate(rs.getString("license_plate"));
                car.setCapacity(rs.getInt("capacity"));
                car.setDriverId(rs.getInt("driver_id"));
                return car;
            }
        } catch (SQLException e) {
            System.out.println(" [ERROR] Failed to fetch car by driver ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // No car found
    }
}
