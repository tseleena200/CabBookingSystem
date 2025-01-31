<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.cabbookingsystem.model.User" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    // ✅ Get session object
    HttpSession userSession = request.getSession(false);

    // ✅ Redirect to login if session is null (user not logged in)
    if (userSession == null || userSession.getAttribute("user") == null) {
        response.sendRedirect("../html/login.html?error=session_expired");
        return;
    }

    // ✅ Retrieve logged-in user details
    User loggedInUser = (User) userSession.getAttribute("user");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home | Mega CityCab</title>
    <link rel="stylesheet" href="/CabBookingSystem_war_exploded/css/style.css">
    <!-- ✅ Link to CSS -->
</head>
<body>

<!-- ✅ Navigation Bar -->
<nav class="navbar">
    <div class="logo">Mega CityCab</div>
    <ul class="nav-links">
        <li><a href="homepage.jsp">Home</a></li>
        <li><a href="booking.jsp">Add Booking</a></li>
        <li><a href="viewBookings.jsp">View Bookings</a></li>
        <li><a href="manageCars.jsp">Manage Cars</a></li>
        <li><a href="calculateBill.jsp">Calculate Bill</a></li>
        <li><a href="help.jsp">Help</a></li>
        <li><a href="logout.jsp" class="logout-btn">Logout</a></li>
    </ul>
</nav>

<!-- ✅ Hero Section -->
<header class="hero">
    <h1>Reserve Your Taxi From Any Location!</h1>
    <p>Book your ride with ease and convenience.</p>
    <a href="#booking" class="btn">Book a Taxi</a>
</header>

<!-- ✅ Booking Section -->
<section id="booking" class="booking-form">
    <h2>Confirm your booking now!</h2>
    <form action="addBookingServlet" method="POST">
        <input type="text" name="name" placeholder="Your Name" required>
        <input type="tel" name="phone" placeholder="Phone" required>
        <input type="email" name="email" placeholder="Email" required>
        <input type="text" name="destination" placeholder="Destination" required>
        <input type="date" name="date" required>
        <input type="time" name="time" required>
        <button type="submit" class="btn">Book a Taxi</button>
    </form>
</section>

<!-- ✅ JavaScript -->
<script src="../js/script.js"></script>

</body>
</html>
