<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.cabbookingsystem.dao.BookingDAO" %>
<%@ page import="com.example.cabbookingsystem.model.Booking" %>

<%
    String orderNumber = request.getParameter("order_number");
    Booking booking = BookingDAO.getInstance().getBookingByOrderNumber(orderNumber);

    if (booking == null) {
%>
<script>
    alert("Error: Booking not found!");
    window.location.href = "view_bookings.jsp"; // Redirect back
</script>
<%
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Booking</title>
    <link rel="stylesheet" href="../css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="../js/script.js" defer></script>
    <style>
        body {
            background-color: #111111; /* Dark background */
            color: #f1f1f1; /* Light text color */
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }

        h2 {
            text-align: center;
            margin-top: 20px;
            color: #d4af37; /* Gold color for header */
        }

        form {
            max-width: 900px;
            margin: 0 auto;
            padding: 20px;
            background-color: #222222;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
        }

        .form-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
        }

        .form-row label {
            flex: 1;
            margin-right: 10px;
            color: #f1f1f1;
            text-align: right;
        }

        .form-row input[type="text"],
        .form-row input[type="date"],
        .form-row input[type="time"],
        .form-row input[type="number"],
        .form-row input[type="email"],
        .form-row select {
            flex: 2;
            padding: 12px;
            margin-bottom: 15px;
            border: 1px solid #444;
            background-color: #333333;
            color: #f1f1f1;
            border-radius: 5px;
        }

        button {
            background-color: #d4af37; /* Gold button */
            color: black;
            padding: 12px 20px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
            margin-top: 20px;
        }

        button:hover {
            background-color: #c5a000;
        }

        .back-button {
            background-color: #444;
            color: #d4af37;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            text-align: center;
            display: inline-block;
            margin-top: 20px;
            cursor: pointer;
        }

        .back-button:hover {
            background-color: #666;
        }
    </style>
</head>
<body>

<h2>Edit Booking - Order#: <%= booking.getOrderNumber() %></h2>

<form id="editBookingForm" action="../editBooking" method="POST">
    <input type="hidden" name="order_number" value="<%= booking.getOrderNumber() %>">

    <div class="form-row">
        <label for="customer_name">Customer Name:</label>
        <input type="text" name="customer_name" id="customer_name" value="<%= booking.getCustomerName() %>" required>
    </div>

    <div class="form-row">
        <label for="customer_email">Customer Email:</label>
        <input type="email" name="customer_email" id="customer_email" value="<%= booking.getCustomerEmail() %>">
    </div>

    <div class="form-row">
        <label for="customer_address">Address:</label>
        <input type="text" name="address" id="customer_address" value="<%= booking.getCustomerAddress() %>" required>
    </div>

    <div class="form-row">
        <label for="phone_number">Phone Number:</label>
        <input type="text" name="telephone" id="phone_number" value="<%= booking.getPhoneNumber() %>" required>
    </div>

    <div class="form-row">
        <label for="destination">Destination:</label>
        <input type="text" name="destination" id="destination" value="<%= booking.getDestination() %>" required>
    </div>

    <div class="form-row">
        <label for="scheduled_date">Scheduled Date:</label>
        <input type="date" name="scheduled_date" id="scheduled_date" value="<%= booking.getScheduledDate() %>" required>
    </div>

    <div class="form-row">
        <label for="scheduled_time">Scheduled Time:</label>
        <input type="time" name="scheduled_time" id="scheduled_time" value="<%= booking.getScheduledTime() %>" required>
    </div>

    <div class="form-row">
        <label for="fare_type">Fare Type:</label>
        <select name="fare_type" id="fare_type">
            <option value="Standard" <%= "Standard".equals(booking.getFareType()) ? "selected" : "" %>>Standard</option>
            <option value="Luxury" <%= "Luxury".equals(booking.getFareType()) ? "selected" : "" %>>Luxury</option>
            <option value="Shared" <%= "Shared".equals(booking.getFareType()) ? "selected" : "" %>>Shared</option>
        </select>
    </div>

    <div class="form-row">
        <label for="base_fare">Base Fare (AED):</label>
        <input type="number" name="base_fare" id="base_fare" value="<%= booking.getBaseFare() %>" step="0.01" required>
    </div>

    <div class="form-row">
        <label for="distance">Distance (KM):</label>
        <input type="number" name="distance" id="distance" value="<%= booking.getDistance() %>" step="0.1" required>
    </div>

    <div class="form-row">
        <label for="tax_rate">Tax Rate (%):</label>
        <input type="number" name="tax_rate" id="tax_rate" value="<%= booking.getTaxRate() %>" step="0.1" required>
    </div>

    <div class="form-row">
        <label for="discount_rate">Discount Rate (%):</label>
        <input type="number" name="discount_rate" id="discount_rate" value="<%= booking.getDiscountRate() %>" step="0.1" required>
    </div>

    <button type="submit">Save Changes</button>
</form>

<a href="view_bookings.jsp" class="back-button">Back to View Bookings</a>

</body>
</html>
