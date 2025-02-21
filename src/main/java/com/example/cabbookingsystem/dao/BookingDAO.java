package com.example.cabbookingsystem.dao;

import com.example.cabbookingsystem.factory.BookingFactory;
import com.example.cabbookingsystem.model.Booking;
import com.example.cabbookingsystem.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookingDAO {
    private static BookingDAO instance;
    private final Connection connection;
    private static final Logger LOGGER = Logger.getLogger(BookingDAO.class.getName());
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

    //  Get Driver ID by Car ID
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
    //  Fetch Driver's Full Name by Driver ID
    public String getDriverNameById(int driverId) {
        String driverName = "Unknown Driver"; // Default if not found
        String query = "SELECT full_name FROM drivers WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                driverName = rs.getString("full_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driverName;
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

    //  Retrieve available cars (For dropdown in `add_booking.jsp`)
    public List<String[]> getAvailableCars() {
        List<String[]> cars = new ArrayList<>();
        String query = "SELECT id, model, license_plate, driver_id FROM cars " +
                "WHERE id NOT IN (SELECT car_id FROM bookings WHERE status = 'Pending' OR status = 'Confirmed')";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String[] carData = {
                        String.valueOf(rs.getInt("id")),
                        rs.getString("model") + " (" + rs.getString("license_plate") + ")",
                        String.valueOf(rs.getInt("driver_id"))
                };
                cars.add(carData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
    public boolean isDriverAvailable(int driverId) {
        String query = "SELECT COUNT(*) FROM bookings WHERE driver_id = ? AND status NOT IN ('Cancelled', 'Completed')";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Return true if no active bookings exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to false if error occurs
    }

    //  Insert New Booking
    public boolean addBooking(Booking booking) {
        String orderNumber = generateOrderNumber();
        booking.setOrderNumber(orderNumber);


        // Retrieve Driver ID
        int driverId = getDriverIdByCarId(booking.getCarId());
        if (driverId == -1) {
            System.out.println(" No driver assigned to Car ID: " + booking.getCarId());
            return false;
        }
        booking.setDriverId(driverId);

        // Check if car is already booked at the same date & time
        if (isCarBooked(booking.getCarId(), booking.getScheduledDate(), booking.getScheduledTime())) {
            System.out.println(" Car ID " + booking.getCarId() + " is already booked on " + booking.getScheduledDate() + " at " + booking.getScheduledTime());
            return false;
        }


        String query = "INSERT INTO bookings (order_number, customer_name, address, telephone, destination, booking_date, " +
                "scheduled_date, scheduled_time, status, car_id, driver_id, customer_registration_number, " +
                "total_amount, confirmed_by_employee, fare_type, base_fare, distance, tax_rate, discount_rate, customer_email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // ✅ 20 Placeholders
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, booking.getOrderNumber());
            stmt.setString(2, booking.getCustomerName());
            stmt.setString(3, booking.getCustomerAddress());
            stmt.setString(4, booking.getPhoneNumber());
            stmt.setString(5, booking.getDestination());
            stmt.setTimestamp(6, new Timestamp(booking.getBookingDate().getTime()));
            stmt.setDate(7, new java.sql.Date(booking.getScheduledDate().getTime()));
            stmt.setString(8, booking.getScheduledTime());
            stmt.setString(9, "Pending");
            stmt.setInt(10, booking.getCarId());
            stmt.setInt(11, booking.getDriverId());
            stmt.setString(12, booking.getCustomerRegistrationNumber());
            stmt.setDouble(13, 0.0);
            stmt.setString(14, null);
            stmt.setString(15, booking.getFareType());
            stmt.setDouble(16, 0.0);
            stmt.setDouble(17, 0.0);
            stmt.setDouble(18, booking.getTaxRate());
            stmt.setDouble(19, booking.getDiscountRate());
            stmt.setString(20, booking.getCustomerEmail());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println(" Booking added successfully! Order Number: " + orderNumber);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get Booking by Order Number
    public Booking getBookingByOrderNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            System.out.println("🚨 Error: Order number is missing or empty!");
            return null;
        }

        System.out.println(" Fetching booking for Order Number: " + orderNumber);

        String query = """
        SELECT b.*, c.model AS model, c.license_plate AS license_plate, d.full_name AS full_name
        FROM bookings b
        JOIN cars c ON b.car_id = c.id
        JOIN drivers d ON b.driver_id = d.id
        WHERE b.order_number = ?;
    """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderNumber.trim());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Booking found: " + rs.getString("order_number"));

                return BookingFactory.createBooking(
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
                        rs.getString("confirmed_by_employee"),
                        rs.getString("fare_type"),
                        rs.getDouble("base_fare"),
                        rs.getDouble("distance"),
                        rs.getDouble("tax_rate"),
                        rs.getDouble("discount_rate"),
                        rs.getString("status"),
                        rs.getString("customer_email"),
                        rs.getString("model"),         // ✅ Fix: Fetch Model
                        rs.getString("license_plate"), // ✅ Fix: Fetch License Plate
                        rs.getString("full_name")      // ✅ Fix: Fetch Driver Name
                );
            } else {
                System.out.println("🚨 Error: No booking found for Order Number: " + orderNumber);
            }
        } catch (SQLException e) {
            System.out.println("🚨 SQL Error in getBookingByOrderNumber(): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateBookingStatus(String orderNumber, String status, String confirmedByEmployee) {
        System.out.println("🔍 DEBUG: Attempting to update booking status...");
        System.out.println("➡ Order Number: " + orderNumber);
        System.out.println("➡ Status: " + status);
        System.out.println("➡ Confirmed By: " + confirmedByEmployee);

        String query = "UPDATE bookings SET status = ?, confirmed_by_employee = ? WHERE order_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setString(2, confirmedByEmployee);
            stmt.setString(3, orderNumber);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("✅ Update Success: Rows Affected = " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //  Update Booking Method
    public boolean updateBooking(String orderNumber, String customerName, String address, String telephone,
                                 String destination, String scheduledDate, String scheduledTime, String fareType,
                                 double baseFare, double distance, double taxRate, double discountRate, String customerEmail) {

        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            System.out.println("🚨 Error: Order number is missing or empty!");
            return false;
        }

        String sql = """
        UPDATE bookings SET 
        customer_name = ?, 
        address = ?, 
        telephone = ?, 
        destination = ?, 
        scheduled_date = ?, 
        scheduled_time = ?, 
        fare_type = ?, 
        base_fare = ?, 
        distance = ?, 
        tax_rate = ?, 
        discount_rate = ?, 
        customer_email = ? 
        WHERE order_number = ?;
    """;

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setString(1, customerName);
            stmt.setString(2, address);
            stmt.setString(3, telephone);
            stmt.setString(4, destination);
            stmt.setString(5, scheduledDate);
            stmt.setString(6, scheduledTime);
            stmt.setString(7, fareType);
            stmt.setDouble(8, baseFare);
            stmt.setDouble(9, distance);
            stmt.setDouble(10, taxRate);
            stmt.setDouble(11, discountRate);
            stmt.setString(12, customerEmail);
            stmt.setString(13, orderNumber);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(" Booking successfully updated for Order#: " + orderNumber);
                return true;
            } else {
                System.out.println(" Error: No rows updated for Order#: " + orderNumber);
            }

        } catch (SQLException e) {
            System.out.println(" SQL Error in updateBooking(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }




    //  Cancel Booking Method
    public boolean cancelBooking(String orderNumber) {
        String query = "UPDATE bookings SET status = 'Cancelled' WHERE order_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Delete Booking Method
    public boolean deleteBooking(String orderNumber) {
        String query = "DELETE FROM bookings WHERE order_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    //  Retrieve the status of a booking by Order Number
    public String getBookingStatus(String orderNumber) {
        String query = "SELECT status FROM bookings WHERE order_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderNumber.trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                System.out.println(" DEBUG: Booking Status Retrieved from DB = [" + status + "] for Order: " + orderNumber);
                return status;
            } else {
                System.out.println(" DEBUG: No booking found for Order Number: " + orderNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //  Fetch all bookings correctly
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = """
            SELECT b.*, c.model AS model, c.license_plate AS license_plate, d.full_name AS full_name
            FROM bookings b
            JOIN cars c ON b.car_id = c.id
            JOIN drivers d ON b.driver_id = d.id;
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = BookingFactory.createBooking(
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
                        rs.getString("confirmed_by_employee"),
                        rs.getString("fare_type"),
                        rs.getDouble("base_fare"),
                        rs.getDouble("distance"),
                        rs.getDouble("tax_rate"),
                        rs.getDouble("discount_rate"),
                        rs.getString("status"),
                        rs.getString("customer_email"),
                        rs.getString("model"), // ✅ Car Model
                        rs.getString("license_plate"), // ✅ License Plate
                        rs.getString("full_name") // ✅ Fixed: Now using full_name instead of driver_name
                );

                booking.setBookingId(rs.getInt("id"));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    //  Update assigned driver for a booking
    public boolean updateDriverForBooking(String orderNumber, int driverId) {
        String query = "UPDATE bookings SET driver_id = ? WHERE order_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            stmt.setString(2, orderNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" [ERROR] Failed to update driver for booking: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBillAlreadyCalculated(String orderNumber) {
        String query = "SELECT total_amount FROM bookings WHERE order_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_amount") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //complete booking
    public boolean completeBooking(String orderNumber) {
        String updateQuery = "UPDATE bookings SET status = 'Completed' WHERE order_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setString(1, orderNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean updateBookingDetails(String orderNumber, double baseFare, double distance, double taxRate, double discountRate, double totalAmount) {
        LOGGER.log(Level.INFO, " Updating Booking: Base Fare: {0}, Distance: {1}, Tax: {2}, Discount: {3}, Total Fare: {4} for Order: {5}",
                new Object[]{baseFare, distance, taxRate, discountRate, totalAmount, orderNumber});

        String query = "UPDATE bookings SET base_fare = ?, distance = ?, tax_rate = ?, discount_rate = ?, total_amount = ? WHERE order_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, baseFare);
            stmt.setDouble(2, distance);
            stmt.setDouble(3, taxRate);
            stmt.setDouble(4, discountRate);
            stmt.setDouble(5, totalAmount);
            stmt.setString(6, orderNumber);

            int rowsUpdated = stmt.executeUpdate();
            LOGGER.log(Level.INFO, " Rows Updated: {0}", rowsUpdated);

            return rowsUpdated > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, " SQL Exception during update: {0}", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}