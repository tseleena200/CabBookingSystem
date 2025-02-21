<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.cabbookingsystem.dao.DriverDAO" %>
<%@ page import="com.example.cabbookingsystem.model.Driver" %>
<%@ page import="java.util.List" %>

<%
    // Fetch all drivers for the dropdown safely
    DriverDAO driverDAO = DriverDAO.getInstance();
    List<Driver> drivers = driverDAO.getAllDrivers();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add New Car</title>
    <link rel="stylesheet" href="../css/adminpanel.css">
</head>
<body>

<div class="container">

    <div class="left-panel">
        <h2>Manage Your Fleet</h2>
        <p>Add a new car and assign a driver.</p>
    </div>

    <div class="right-panel">
        <h1>Add New Car</h1>
        <form id="addCarForm" action="../addCar" method="POST">
            <div class="form-group">
                <label>Car Model:</label>
                <input type="text" id="car_model" name="car_model" required>
            </div>

            <div class="form-group">
                <label>Number Plate:</label>
                <input type="text" id="license_plate" name="license_plate" required>
            </div>

            <div class="form-group">
                <label>Capacity:</label>
                <input type="number" id="capacity" name="capacity" min="1" required>
            </div>

            <div class="form-group">
                <label>Assign Driver:</label>
                <select id="driver_id" name="driver_id" required>
                    <option value="">Select a Driver</option>
                    <% if (drivers != null) { %>
                    <% for (Driver driver : drivers) { %>
                    <option value="<%= driver.getId() %>"><%= driver.getFullName() %> (ID: <%= driver.getId() %>)</option>
                    <% } %>
                    <% } else { %>
                    <option value="">No drivers available</option>
                    <% } %>
                </select>
            </div>

            <button type="submit">Add Car</button>
            <button type="button" class="cancel-btn" onclick="window.location.href='admin_panel.jsp'">Cancel</button>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="../js/script.js"></script>

</body>
</html>
