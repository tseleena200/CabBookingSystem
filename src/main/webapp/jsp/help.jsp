<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help | Mega CityCab</title>

    <!-- External Styles -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.css">
    <link rel="stylesheet" href="../css/help.css">

    <!-- Swiper.js for Carousel -->
    <script src="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.js"></script>

    <!-- Custom styles for this page -->
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f7f6;
            color: #333;
        }

        header {
            background-color: #ffcc00;
            color: white;
            padding: 15px;
            text-align: center;
        }

        header h1 {
            margin: 0;
        }

        nav {
            background-color: #333;
            padding: 10px;
            position: sticky;
            top: 0;
        }

        nav ul {
            list-style-type: none;
            padding: 0;
            margin: 0;
            display: flex;
            justify-content: center;
        }

        nav ul li {
            margin: 0 15px;
        }

        nav ul li a {
            color: white;
            text-decoration: none;
            font-weight: bold;
            padding: 10px 20px;
            transition: background-color 0.3s ease;
        }

        nav ul li a:hover {
            background-color: #ffcc00;
            border-radius: 5px;
        }

        .container {
            width: 80%;
            margin: 0 auto;
            padding: 30px;
        }

        .help-section {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
        }

        .help-section h2 {
            color: #333;
            font-size: 24px;
            margin-bottom: 20px;
        }

        .help-section p {
            font-size: 16px;
            line-height: 1.6;
        }

        .contact-form input, .contact-form textarea {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
            border: 1px solid #ccc;
        }

        .contact-form button {
            background-color: #ffcc00;
            border: none;
            padding: 12px 20px;
            font-size: 16px;
            cursor: pointer;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .contact-form button:hover {
            background-color: #e6b800;
        }

        .faq-section {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .faq-section h2 {
            color: #333;
            font-size: 24px;
            margin-bottom: 20px;
        }

        .faq-section ul {
            list-style-type: none;
            padding: 0;
        }

        .faq-section ul li {
            font-size: 16px;
            margin-bottom: 10px;
            cursor: pointer;
            color: #ffcc00;
            text-decoration: underline;
        }

        .faq-answer {
            display: none;
            margin-top: 10px;
            font-size: 14px;
            line-height: 1.5;
            padding: 10px;
            background-color: #f9f9f9;
            border-radius: 5px;
            border: 1px solid #e2e2e2;
        }

        footer {
            background-color: #333;
            color: white;
            padding: 20px;
            text-align: center;
            position: fixed;
            bottom: 0;
            width: 100%;
        }

        .faq-section ul li:hover {
            color: #333;
        }
    </style>
</head>
<body>

<header>
    <h1>Welcome to Mega CityCab Help Center</h1>
</header>

<nav>
    <ul>
        <li><a href="homepage.jsp">Dashboard</a></li>
        <li><a href="add_booking.jsp">Add Booking</a></li>
        <li><a href="view_bookings.jsp">View Bookings</a></li>
        <li><a href="manage.jsp">Manage Drivers</a></li>
        <li><a href="calculate_bill.jsp">Calculate Bill</a></li>
        <li><a href="help.jsp">Help</a></li>
        <li><a href="logout.jsp" class="logout-btn">Logout</a></li>
    </ul>
</nav>

<div class="container">
    <!-- Help Section -->
    <div class="help-section">
        <h2>How to Use the Employee Dashboard</h2>
        <p>Our employee dashboard is designed to help you manage bookings, track driver schedules, and assist customers with their needs. Here's how you can navigate through it:</p>
        <ul>
            <li><strong>Dashboard:</strong> View all ongoing and upcoming bookings at a glance.</li>
            <li><strong>Add Booking:</strong> Create new bookings by entering customer and journey details.</li>
            <li><strong>Manage Drivers:</strong> Update and manage driver assignments, availability, and performance.</li>
            <li><strong>Calculate Bill:</strong> Quickly generate a bill for completed bookings (after confirming the booking).</li>
            <li><strong>Cancel Booking:</strong> Cancel any booking that needs to be stopped before the trip begins.</li>
        </ul>
    </div>

    <!-- FAQ Section -->
    <div class="faq-section">
        <h2>Frequently Asked Questions (FAQs)</h2>
        <ul>
            <li onclick="toggleAnswer('faq1')">How do I add a new booking?</li>
            <div id="faq1" class="faq-answer">
                <p>To add a booking, click on "Add Booking" in the navigation menu, fill out the necessary details such as customer name, address, etc.., and then click on "Book a Taxi" to confirm.</p>
            </div>
            <li onclick="toggleAnswer('faq2')">What should I do if a customer complains about their ride?</li>
            <div id="faq2" class="faq-answer">
                <p>If a customer complains about their ride, please document the issue and report it to the manager for resolution.</p>
            </div>
            <li onclick="toggleAnswer('faq3')">How can I update a driver's availability?</li>
            <div id="faq3" class="faq-answer">
                <p>Only admins can update a driver's availability. Employees should contact an admin for any changes related to driver assignments or availability.</p>
            </div>

            <li onclick="toggleAnswer('faq4')">Where can I view the past bookings and their details?</li>
            <div id="faq4" class="faq-answer">
                <p>You can view past bookings under the "View Bookings" section. All historical booking information will be listed there.</p>
            </div>
        </ul>
    </div>

    <!-- Contact Form Section -->
    <div class="help-section contact-form">
        <h2>Need Further Assistance?</h2>
        <p>If you couldn't find the answer you're looking for, please feel free to reach out to our support team. We are here to help you!</p>
        <form action="HelpServlet" method="POST">
            <input type="text" name="employeeName" placeholder="Your Name" required>
            <input type="email" name="employeeEmail" placeholder="Your Email" required>
            <textarea name="message" rows="4" placeholder="Describe your issue or inquiry" required></textarea>
            <button type="submit">Submit Request</button>
        </form>
    </div>
</div>

<footer>
    <p>&copy; 2025 Mega CityCab. All rights reserved.</p>
</footer>

<!-- JavaScript for FAQ Toggle -->
<script>
    function toggleAnswer(id) {
        var answer = document.getElementById(id);
        if (answer.style.display === "none" || answer.style.display === "") {
            answer.style.display = "block";
        } else {
            answer.style.display = "none";
        }
    }
</script>

</body>
</html>
