<%@ page import="java.util.List, com.example.cabbookingsystem.dao.CarDAO, com.example.cabbookingsystem.model.Car" %>
<html>
<head>
    <title>View Cars | Mega CityCab</title>
    <link rel="stylesheet" href="../css/homepage.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #000;
            color: white;
            margin: 0;
            padding: 0;
        }

        .content {
            padding-top: 100px;
            max-width: 90%;
            margin: auto;
        }

        h2 {
            margin-top: 20px;
            font-size: 26px;
            font-weight: bold;
        }

        .scrollable-table {
            max-height: 400px;
            overflow-y: auto;
            margin-top: 20px;
            border: 2px solid #ffffff;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: black;
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
            position: sticky;
            top: 0;
        }
    </style>
</head>
<body>
<nav class="navbar">
    <div class="logo-container">
        <img src="../images/taxilogo.png" alt="Mega CityCab Logo" class="logo-img">
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
<!-- ✅ Employee Page Header -->
<div class="content">
    <h2> Available Cars</h2>

    <!--  Cars Table -->
    <div class="scrollable-table">
        <table>
            <thead>
            <tr>
                <th>Car Model</th>
                <th>License Plate</th>
                <th>Capacity</th>
                <th>Driver ID</th>
            </tr>
            </thead>
            <tbody>
            <%
                CarDAO carDAO = CarDAO.getInstance();
                List<Car> cars = carDAO.getAllCars();
                for (Car car : cars) {
            %>
            <tr>
                <td><%= car.getModel() %></td>
                <td><%= car.getLicensePlate() %></td>
                <td><%= car.getCapacity() %></td>
                <td><%= car.getDriverId() == null ? "Unassigned" : car.getDriverId() %></td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
