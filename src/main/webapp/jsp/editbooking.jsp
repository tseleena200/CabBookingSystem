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
    <script src="../js/script.js" defer></script> <!-- ✅ Load external JS file -->
</head>
<body>

<h2>Edit Booking - Order#: <%= booking.getOrderNumber() %></h2>

<form id="editBookingForm" action="../editBooking" method="POST">
    <input type="hidden" name="order_number" value="<%= booking.getOrderNumber() %>">

    <label>Customer Name:</label>
    <input type="text" name="customer_name" id="customer_name" value="<%= booking.getCustomerName() %>" required>

    <label>Address:</label>
    <input type="text" name="address" id="customer_address" value="<%= booking.getCustomerAddress() %>" required>

    <label>Phone Number:</label>
    <input type="text" name="telephone" id="phone_number" value="<%= booking.getPhoneNumber() %>" required>

    <label>Destination:</label>
    <input type="text" name="destination" id="destination" value="<%= booking.getDestination() %>" required>

    <label>Scheduled Date:</label>
    <input type="date" name="scheduled_date" id="scheduled_date" value="<%= booking.getScheduledDate() %>" required>

    <label>Scheduled Time:</label>
    <input type="time" name="scheduled_time" id="scheduled_time" value="<%= booking.getScheduledTime() %>" required>

    <label>Fare Type:</label>
    <select name="fare_type" id="fare_type">
        <option value="Standard" <%= "Standard".equals(booking.getFareType()) ? "selected" : "" %>>Standard</option>
        <option value="Luxury" <%= "Luxury".equals(booking.getFareType()) ? "selected" : "" %>>Luxury</option>
        <option value="Shared" <%= "Shared".equals(booking.getFareType()) ? "selected" : "" %>>Shared</option>
    </select>

    <label>Base Fare (AED):</label>
    <input type="number" name="base_fare" id="base_fare" value="<%= booking.getBaseFare() %>" step="0.01" required>

    <label>Distance (KM):</label>
    <input type="number" name="distance" id="distance" value="<%= booking.getDistance() %>" step="0.1" required>

    <label>Tax Rate (%):</label>
    <input type="number" name="tax_rate" id="tax_rate" value="<%= booking.getTaxRate() %>" step="0.1" required>

    <label>Discount Rate (%):</label>
    <input type="number" name="discount_rate" id="discount_rate" value="<%= booking.getDiscountRate() %>" step="0.1" required>

    <label>Customer Email:</label>
    <input type="email" name="customer_email" id="customer_email" value="<%= booking.getCustomerEmail() %>">

    <button type="submit">Save Changes</button>
</form>

</body>
</html>
