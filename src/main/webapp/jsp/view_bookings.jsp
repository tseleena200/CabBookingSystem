<%@ page import="java.util.List, com.example.cabbookingsystem.dao.BookingDAO, com.example.cabbookingsystem.model.Booking" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Bookings | Mega CityCab</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/homepage.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="../js/script.js" defer></script>
    <!-- ✅ Load external JS file -->

    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #000;
            color: white;
            padding-top: 80px;
        }
        h2 {
            margin-top: 20px;
            font-size: 26px;
            font-weight: bold;
        }
        table {
            width: 90%;
            margin: 20px auto;
            border-collapse: collapse;
            background: black;
            border: 2px solid #ffffff;
            overflow-x: auto;
        }
        th, td {
            padding: 12px;
            border: 1px solid #ffffff;
            text-align: center;
        }
        th {
            background-color: #ffffff;
            color: black;
            font-weight: bold;
        }
        .manage-btn {
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            border: none;
            border-radius: 5px;
            color: black;
            font-weight: bold;
            transition: 0.3s;
        }
        .confirm-btn {
            background-color: #FFD700;
        }
        .confirm-btn:hover {
            background-color: #FFC107;
        }
        .status-confirmed {
            color: green;
            font-weight: bold;
        }
    </style>
</head>
<body>

<!-- Navigation Bar -->
<nav class="navbar">
    <div class="logo-container">
        <img src="<%= request.getContextPath() %>/images/taxilogo.png" alt="Mega CityCab Logo" class="logo-img">
        <span class="logo-text">MEGA CITYCAB</span>
    </div>
    <ul class="nav-links">
        <li><a href="homepage.jsp">Home</a></li>
        <li><a href="add_booking.jsp">Add Booking</a></li>
        <li><a href="view_bookings.jsp">View Bookings</a></li>
        <li><a href="manage.jsp">Manage</a></li>
        <li><a href="calculate_bill.jsp">Calculate Bill</a></li>
        <li><a href="help.jsp">Help</a></li>
        <li><a href="logout.jsp" class="logout-btn">Logout</a></li>
    </ul>
</nav>

<h2>Booking Management</h2>

<!-- Bookings Table -->
<table>
    <thead>
    <tr>
        <th>Order Number</th>
        <th>Customer Name</th>
        <th>Destination</th>
        <th>Scheduled Date</th>
        <th>Scheduled Time</th>
        <th>Car ID</th>
        <th>Car Model</th> <!-- ✅ Added Car Model -->
        <th>License Plate</th> <!-- ✅ Added License Plate -->
        <th>Driver ID</th>
        <th>Driver Name</th> <!-- ✅ Added Driver Name -->
        <th>Total Amount</th>
        <th>Status</th>
        <th>Confirmed By</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <%
        BookingDAO bookingDAO = BookingDAO.getInstance();
        List<Booking> bookings = bookingDAO.getAllBookings();
        for (Booking booking : bookings) {
    %>
    <tr>
        <td><%= booking.getOrderNumber() %></td>
        <td><%= booking.getCustomerName() %></td>
        <td><%= booking.getDestination() %></td>
        <td><%= booking.getScheduledDate() %></td>
        <td><%= booking.getScheduledTime() %></td>
        <td><%= booking.getCarId() %></td>
        <td><%= booking.getModel() %></td>
        <td><%= booking.getLicensePlate() %></td>
        <td><%= booking.getDriverId() %></td>
        <td><%= booking.getFullName() %></td>
        <td><%= booking.getTotalAmount() %></td>
        <td id="status_<%= booking.getOrderNumber() %>"><%= booking.getStatus() %></td>
        <td>
            <% if (!"Confirmed".equals(booking.getStatus())) { %>
            <button class="confirm-booking-btn" data-order-number="<%= booking.getOrderNumber() %>">Confirm</button>
            <% } %>
        </td>
        <td>
            <a href="<%= request.getContextPath() %>/jsp/editbooking.jsp?order_number=<%= booking.getOrderNumber() %>" class="edit-booking-btn">
                ✏️ Edit
            </a>

            <button class="delete-booking-btn" data-order-number="<%= booking.getOrderNumber() %>">🗑️ Delete</button>

            <% if (!"Cancelled".equals(booking.getStatus())) { %>
            <button class="cancel-booking-btn" data-order-number="<%= booking.getOrderNumber() %>">🚫 Cancel</button>
            <% } %>
        </td>
    </tr>
    <% } %> <!-- ✅ Properly closing the loop -->
    </tbody>

</table>

</body>
</html>
