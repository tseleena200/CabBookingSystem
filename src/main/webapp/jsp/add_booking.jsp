<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.cabbookingsystem.model.User" %>
<%@ page import="com.example.cabbookingsystem.dao.BookingDAO" %>

<%
    User loggedInUser = (User) session.getAttribute("user");

    //  Redirect if not logged in or not an employee
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
    <link rel="stylesheet" href="../css/style.css">

    <!--   SweetAlert2 Library -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <!--  Link JavaScript  -->
    <script src="../js/script.js" defer></script>
</head>
<body>

<h1>Add New Booking</h1>
<a href="../logout.jsp">Logout</a>

<h2>New Customer Booking</h2>

<!--  Booking Form -->
<form id="bookingForm" method="POST" action="../addBooking">

    <label>Customer Name:</label>
    <input type="text" id="customer_name" name="customer_name" required>

    <label>Customer Address:</label>
    <input type="text" id="customer_address" name="customer_address" required>

    <label>Telephone:</label>
    <input type="tel" id="phone_number" name="phone_number" pattern="[0-9]+" required title="Only numbers allowed">

    <label>Destination:</label>
    <input type="text" id="destination" name="destination" required>

    <label>Scheduled Date:</label>
    <input type="date" id="scheduled_date" name="scheduled_date" required>

    <label>Scheduled Time:</label>
    <input type="time" id="scheduled_time" name="scheduled_time" required>

    <label>Select Car:</label>
    <select id="car_id" name="car_id" required>
        <option value="">-- Select Car --</option>
        <%
            BookingDAO bookingDAO = BookingDAO.getInstance();
            for (String[] car : bookingDAO.getAvailableCars()) {
        %>
        <option value="<%= car[0] %>"><%= car[1] %> (Driver ID: <%= car[2] %>)</option>
        <%
            }
        %>
    </select>


    <input type="hidden" id="employee_reg_number" name="employee_reg_number" value="<%= employeeRegNumber %>">

    <button type="submit">Add Booking</button>
</form>

</body>
</html>
