<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.cabbookingsystem.model.User" %>
<%@ page import="com.example.cabbookingsystem.dao.BookingDAO" %>
<%@ page import="com.example.cabbookingsystem.dao.DriverDAO" %>
<%@ page import="java.util.List" %>

<%
    User loggedInUser = (User) session.getAttribute("user");

    // Redirect if not logged in or not an employee
    if (loggedInUser == null || !"employee".equals(loggedInUser.getRole())) {
        response.sendRedirect("../html/login.html?error=unauthorized");
        return;
    }

    String employeeRegNumber = loggedInUser.getCustomerRegistrationNumber(); // Retrieve Registration Number
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Booking</title>
    <link rel="stylesheet" href="../css/bookingform.css">

    <!-- SweetAlert2 Library -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <!-- Link JavaScript -->
    <script src="../js/script.js" defer></script>
</head>
<body>

<!--  Navbar -->
<nav class="navbar">
    <div class="logo-container">
        <img src="../images/taxilogo.png" alt="Mega CityCab Logo" class="logo-img">
        <span class="logo-text">MEGA CITYCAB</span>
    </div>
    <ul class="nav-links">
        <li><a href="homepage.jsp">Home</a></li>
        <li><a href="add_booking.jsp" class="active">Add Booking</a></li>
        <li><a href="view_bookings.jsp">View Bookings</a></li>
        <li><a href="manage.jsp">Manage</a></li>
        <li><a href="calculate_bill.jsp">Calculate Bill</a></li>
        <li><a href="help.jsp">Help</a></li>

        <li><a href="logout.jsp" class="logout-btn">Logout</a></li>
    </ul>
</nav>
<!-- Form Container -->
<div class="container">
    <h2>New Customer Booking</h2>

    <form id="bookingForm" method="POST" action="../addBooking">
        <label>Customer Name:</label>
        <input type="text" id="customer_name" name="customer_name">

        <label>Customer Email:</label>
        <input type="email" id="customer_email" name="customer_email">

        <label>Customer Address:</label>
        <input type="text" id="customer_address" name="customer_address">

        <label>Telephone:</label>
        <input type="tel" id="phone_number" name="phone_number" pattern="[0-9]+" title="Only numbers allowed">

        <label>Destination:</label>
        <input type="text" id="destination" name="destination">

        <label>Scheduled Date:</label>
        <input type="date" id="scheduled_date" name="scheduled_date">

        <label>Scheduled Time:</label>
        <input type="time" id="scheduled_time" name="scheduled_time">

        <!-- Add Car Selection -->
        <label>Select Car:</label>
        <select id="car_id" name="car_id"  onchange="updateDriverId()">
            <option value="">-- Select Car --</option>
            <%
                BookingDAO bookingDAO = BookingDAO.getInstance();
                List<String[]> availableCars = bookingDAO.getAvailableCars();

                if (availableCars == null || availableCars.isEmpty()) { %>
            <option value="">No Available Cars</option>
            <% } else {
                for (String[] car : availableCars) {
                    int driverId = Integer.parseInt(car[2]); // Assuming car[2] holds the driver ID
                    String driverName = bookingDAO.getDriverNameById(driverId); // Fetch the driver's full name
            %>
            <option value="<%= car[0] %>" data-driver-id="<%= driverId %>">
                <%= car[1] %> (Driver: <%= driverName %>)
            </option>
            <% } } %>
        </select>

        <label>Select Fare Type:</label>
        <select id="fare_type" name="fare_type">
            <option value="">-- Select Fare Type --</option>
            <option value="Standard">Standard</option>
            <option value="Luxury">Luxury</option>
            <option value="Shared">Shared</option>
        </select>

        <!-- Hidden field to pass Employee Registration Number -->
        <input type="hidden" id="employee_reg_number" name="employee_reg_number" value="<%= employeeRegNumber %>">
        <!-- Hidden field to store the driver ID automatically -->
        <input type="hidden" id="driver_id" name="driver_id">
        <button type="submit" class="btn">Add Booking</button>
    </form>
</div>



</body>
</html>
