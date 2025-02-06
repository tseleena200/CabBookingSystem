package com.example.cabbookingsystem.dao;

import com.example.cabbookingsystem.model.Booking;
import com.example.cabbookingsystem.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private static BookingDAO instance;
    private final Connection connection;

    private BookingDAO() {
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    public static BookingDAO getInstance() {
        if (instance == null) {
            synchronized (BookingDAO.class) {
                if (instance == null) {
                    instance = new BookingDAO();
                }
            }
        }
        return instance;
    }

    //  Generate Unique Order Number
    private String generateOrderNumber() {
        String orderNumber;
        do {
            int randomNum = (int) (Math.random() * 100000);
            orderNumber = "ORD-" + randomNum;
        } while (orderNumberExists(orderNumber));
        return orderNumber;
    }

    private boolean orderNumberExists(String orderNumber) {
        String query = "SELECT COUNT(*) FROM bookings WHERE order_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get Driver ID by Car ID
    public int getDriverIdByCarId(int carId) {
        String query = "SELECT driver_id FROM cars WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("driver_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //  Retrieve available cars (For dropdown in add_booking.jsp)
    public List<String[]> getAvailableCars() {
        List<String[]> cars = new ArrayList<>();
        String query = "SELECT id, model, license_plate, driver_id FROM cars WHERE id NOT IN (SELECT car_id FROM bookings WHERE status = 'Pending' OR status = 'Confirmed')";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String[] carData = {
                        String.valueOf(rs.getInt("id")), // Car ID
                        rs.getString("model") + " (" + rs.getString("license_plate") + ")", // Display model + plate
                        String.valueOf(rs.getInt("driver_id")) // Driver ID
                };
                cars.add(carData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    //  Insert New Booking
    public boolean addBooking(Booking booking) {
        String orderNumber = generateOrderNumber();
        booking.setOrderNumber(orderNumber);

        // Retrieve Driver ID
        int driverId = getDriverIdByCarId(booking.getCarId());
        if (driverId == -1) {
            System.out.println("❌ No driver assigned to Car ID: " + booking.getCarId());
            return false;
        }
        booking.setDriverId(driverId);

        // Check if car is already booked at the same date & time
        if (isCarBooked(booking.getCarId(), booking.getScheduledDate(), booking.getScheduledTime())) {
            System.out.println("❌ Car ID " + booking.getCarId() + " is already booked on " + booking.getScheduledDate() + " at " + booking.getScheduledTime());
            return false;
        }

        String query = "INSERT INTO bookings (order_number, customer_name, address, telephone, destination, booking_date, scheduled_date, scheduled_time, status, car_id, driver_id, customer_registration_number, total_amount, confirmed_by_employee) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, booking.getOrderNumber());
            stmt.setString(2, booking.getCustomerName());
            stmt.setString(3, booking.getCustomerAddress());
            stmt.setString(4, booking.getPhoneNumber());
            stmt.setString(5, booking.getDestination());
            stmt.setTimestamp(6, new Timestamp(booking.getBookingDate().getTime()));
            stmt.setDate(7, new java.sql.Date(booking.getScheduledDate().getTime()));
            stmt.setString(8, booking.getScheduledTime()); // ✅ Scheduled Time
            stmt.setString(9, booking.getStatus());
            stmt.setInt(10, booking.getCarId());
            stmt.setInt(11, booking.getDriverId());
            stmt.setString(12, booking.getCustomerRegistrationNumber());
            stmt.setDouble(13, booking.getTotalAmount());
            stmt.setString(14, booking.getConfirmedByEmployee()); // ✅ FIXED Column Name

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    booking.setBookingId(generatedKeys.getInt(1));
                }
                System.out.println("✅ Booking added successfully! Order Number: " + orderNumber);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Retrieve all bookings
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings ORDER BY booking_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getString("order_number"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("telephone"),
                        rs.getString("destination"),
                        rs.getTimestamp("booking_date"),
                        rs.getDate("scheduled_date"),
                        rs.getString("scheduled_time"),
                        rs.getInt("car_id"),
                        rs.getInt("driver_id"),
                        rs.getString("customer_registration_number"),
                        rs.getString("confirmed_by_employee") // ✅ FIXED Column Name
                );
                booking.setBookingId(rs.getInt("id"));
                booking.setStatus(rs.getString("status"));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    //  Update booking status
    public boolean updateBookingStatus(int bookingId, String status, String confirmedByEmployee) {
        String query = "UPDATE bookings SET status = ?, confirmed_by_employee = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setString(2, confirmedByEmployee);
            stmt.setInt(3, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //  Check if a Car is Already Booked at the Same Date & Time
    public boolean isCarBooked(int carId, Date scheduledDate, String scheduledTime) {
        String query = "SELECT COUNT(*) FROM bookings WHERE car_id = ? AND scheduled_date = ? AND scheduled_time = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, carId);
            stmt.setDate(2, new java.sql.Date(scheduledDate.getTime()));
            stmt.setString(3, scheduledTime);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
