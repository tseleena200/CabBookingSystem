<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Homepage | Mega CityCab</title>

    <!--  External Styles -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.css">
    <link rel="stylesheet" href="../css/homepage.css">

    <!-- Swiper.js for Carousel -->
    <script src="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.js"></script>
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
        <li><a href="manage_cars.jsp">Manage Cars</a></li>
        <li><a href="calculate_bill.jsp">Calculate Bill</a></li>
        <li><a href="help.jsp">Help</a></li>
        <li><a href="logout.jsp" class="logout-btn">Logout</a></li>
    </ul>
</nav>

<!--  Hero Section - Swiper Carousel -->
<section class="hero">
    <div class="swiper mySwiper">
        <div class="swiper-wrapper">
            <div class="swiper-slide">
                <img src="../images/slider1.jpg" alt="Taxi Service">
                <div class="overlay-text">
                    <h1>Reserve Your Taxi From Any Location!</h1>
                    <p>Book your ride with ease and convenience.</p>
                    <a href="add_booking.jsp" class="btn">Book a Taxi</a>
                </div>
            </div>
            <div class="swiper-slide">
                <img src="../images/slider2.jpg" alt="City Rides">
                <div class="overlay-text">
                    <h1>Fast & Reliable Cab Service</h1>
                    <p>Reach your destination safely and on time.</p>
                    <a href="add_booking.jsp" class="btn">Book Now</a>
                </div>
            </div>
            <div class="swiper-slide">
                <img src="../images/slider3.jpg" alt="Luxury Rides">
                <div class="overlay-text">
                    <h1>Luxury Rides at Affordable Prices</h1>
                    <p>Enjoy premium comfort at the best rates.</p>
                    <a href="add_booking.jsp" class="btn">Get Started</a>
                </div>
            </div>
        </div>
        <!-- Pagination & Navigation Buttons -->
        <div class="swiper-pagination"></div>
        <div class="swiper-button-next"></div>
        <div class="swiper-button-prev"></div>
    </div>
</section>

<section class="booking-form">
    <h2>Confirm Your Booking Now!</h2>
    <form action="BookingServlet" method="POST">
        <div class="form-row">
            <input type="text" name="orderNumber" placeholder="Order Number" required>
            <input type="text" name="customerName" placeholder="Customer Name" required>
        </div>
        <div class="form-row">
            <input type="text" name="address" placeholder="Address" required>
            <input type="tel" name="phone" placeholder="Phone Number" required>
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
        <button type="submit">Book a Taxi</button>
    </form>
</section>


<!--  JavaScript for Swiper -->
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
