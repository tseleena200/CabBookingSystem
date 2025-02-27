<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calculate Bill - Mega CityCab</title>
    <link rel="stylesheet" href="../css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="../js/script.js" defer></script>
    <link rel="stylesheet" href="../css/bookingform.css">
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

<!--  Bill Form -->
<div class="bill-container">
    <h1>Calculate and Print Bill</h1>

    <form id="billForm">
        <label>Enter Order Number:</label>
        <input type="text" id="order_number" name="order_number" required
               value="<%= request.getParameter("order_number") != null ? request.getParameter("order_number") : "" %>">

        <label>Enter Distance (KM):</label>
        <input type="number" id="distance" name="distance" step="0.1">

        <label>Enter Base Fare:</label>
        <input type="number" id="base_fare" name="base_fare" step="0.1">

        <label>Enter Tax Rate (%):</label>
        <input type="number" id="tax_rate" name="tax_rate" step="0.1">

        <label>Enter Discount Rate (%):</label>
        <input type="number" id="discount_rate" name="discount_rate" step="0.1">

        <button type="submit" class="bill-btn">Calculate Bill</button>
    </form>

    <!--  Bill Summary Section  -->
    <div id="billSummary" class="bill-summary" style="display: none;">
        <h2>Billing Receipt</h2>
        <div class="receipt">
            <p><strong>Order Number:</strong> <span id="summaryOrder"></span></p>
            <p><strong>Base Fare:</strong> AED <span id="summaryBaseFare"></span></p>
            <p><strong>Distance:</strong> <span id="summaryDistance"></span> KM</p>
            <p><strong>Tax Amount:</strong> AED <span id="summaryTax"></span></p>
            <p><strong>Discount Applied:</strong> AED <span id="summaryDiscount"></span></p>
            <hr>
            <p class="total-amount"><strong>Total Fare:</strong> AED <span id="summaryTotal"></span></p>
        </div>
    </div>
</div>



</body>
</html>
