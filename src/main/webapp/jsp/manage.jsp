<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manage Cars & Drivers</title>
    <link rel="stylesheet" href="../css/homepage.css"> <!-- Ensure CSS is linked -->
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #000000;
            font-family: Arial, sans-serif;
        }
        .container {
            text-align: center;
        }
        .manage-btn {
            display: block;
            width: 300px;
            padding: 20px;
            margin: 20px auto;
            font-size: 20px;
            font-weight: bold;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: 0.3s;
        }
        .manage-cars {
            background-color: #FFCC00;
            color: #000000;
        }
        .manage-drivers {
            background-color: #FFCC00;
            color: #070707;
        }
        .manage-btn:hover {
            opacity: 0.8;
        }
    </style>
</head>
<body>
<!--  Navigation Bar -->
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
<div class="container">
    <h1>Manage Cars & Drivers</h1>
    <button class="manage-btn manage-cars" onclick="location.href='manage_cars_employee.jsp'"> Manage Cars</button>
    <button class="manage-btn manage-drivers" onclick="location.href='manage_drivers_employee.jsp'"> Manage Drivers</button>
</div>

</body>
</html>
