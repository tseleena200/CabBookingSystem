<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Dashboard | Mega CityCab</title>

    <!-- External Styles -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.css">
    <link rel="stylesheet" href="../css/homepage.css">

    <!-- Swiper.js for Carousel -->
    <script src="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.js"></script>
</head>
<body>

<!-- Navigation Bar -->
<nav class="navbar">
    <div class="logo-container">
        <img src="../images/taxilogo.png" alt="Mega CityCab Logo" class="logo-img">
        <span class="logo-text">MEGA CITYCAB</span>
    </div>
    <ul class="nav-links">
        <li><a href="homepage.jsp">Dashboard</a></li>
        <li><a href="add_booking.jsp">Add Booking</a></li>
        <li><a href="view_bookings.jsp">View Bookings</a></li>
        <li><a href="manage.jsp">Manage Drivers</a></li>
        <li><a href="calculate_bill.jsp">Calculate Bill</a></li>
        <li><a href="help.jsp">Help</a></li>
        <li><a href="logout.jsp" class="logout-btn">Logout</a></li>
    </ul>
</nav>

<!-- Hero Section - Swiper Carousel -->
<section class="hero">
    <div class="swiper mySwiper">
        <div class="swiper-wrapper">
            <div class="swiper-slide">
                <img src="../images/slider1.jpg" alt="Driver Management">
                <div class="overlay-text">
                    <h1>Manage Bookings and Drivers</h1>
                    <p>View, update, or cancel bookings easily.</p>
                    <a href="view_bookings.jsp" class="btn">Manage Bookings</a>
                </div>
            </div>
            <div class="swiper-slide">
                <img src="../images/slider2.jpg" alt="City Rides">
                <div class="overlay-text">
                    <h1>Track and Manage Your Drivers</h1>
                    <p>Ensure drivers are available and ready for tasks.</p>
                    <a href="manage.jsp" class="btn">Manage Drivers</a>
                </div>
            </div>
            <div class="swiper-slide">
                <img src="../images/slider3.jpg" alt="Customer Service">
                <div class="overlay-text">
                    <h1>Assist Customers with Ease</h1>
                    <p>Help customers with bookings, payments, and more.</p>
                    <a href="help.jsp" class="btn">Help</a>
                </div>
            </div>
            <div class="swiper-slide">
                <img src="../images/eugene-chystiakov-B-h3so_5UKA-unsplash.jpg" alt="City Rides">
                <div class="overlay-text">
                    <h1>Add New Bookings</h1>
                    <p>Add New Customer Bookings With Ease! .</p>
                    <a href="add_booking.jsp" class="btn">Add Booking</a>
                </div>
            </div>
        </div>
        <!-- Pagination & Navigation Buttons -->
        <div class="swiper-pagination"></div>
        <div class="swiper-button-next"></div>
        <div class="swiper-button-prev"></div>
    </div>
</section>

<!-- Dashboard Section -->
<section class="booking-form">
    <h2>Manage and Assist Your Tasks</h2>
    <form action="BookingServlet" method="POST">
        <div class="form-row">
            <input type="text" name="orderNumber" placeholder="Order Number" required>
            <input type="text" name="customerName" placeholder="Customer Name" required>
        </div>
        <div class="form-row">
            <input type="text" name="address" placeholder="Customer Address" required>
            <input type="tel" name="phone" placeholder="Customer Phone Number" required>
        </div>
        <div class="form-row">
            <input type="number" name="passengerCount" placeholder="Passenger Count" min="1" required>
            <input type="text" name="startDestination" placeholder="Start Destination" required>
        </div>
        <div class="form-row">
            <input type="text" name="endDestination" placeholder="End Destination" required>
            <input type="date" name="bookingDate" required>
        </div>
        <div class="form-row">
            <input type="time" name="bookingTime" required>
        </div>
        <button type="submit">Create New Booking</button>
    </form>
</section>

<!-- JavaScript for Swiper -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        var swiper = new Swiper(".mySwiper", {
            loop: true,
            autoplay: {
                delay: 4000,
                disableOnInteraction: false,
            },
            pagination: {
                el: ".swiper-pagination",
                clickable: true,
            },
            navigation: {
                nextEl: ".swiper-button-next",
                prevEl: ".swiper-button-prev",
            },
        });
    });
</script>

</body>
</html>
