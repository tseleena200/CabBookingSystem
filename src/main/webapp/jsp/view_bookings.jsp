<%@ page import="java.util.List, com.example.cabbookingsystem.dao.BookingDAO, com.example.cabbookingsystem.model.Booking" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Bookings | Mega CityCab</title>
    <link rel="stylesheet" href="../css/homepage.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #000;
            color: white;
            padding-top: 80px; /* Push content below navbar */
        }
        h2 {
            margin-top: 20px;
            font-size: 26px;
            font-weight: bold;
        }
        table {
            width: 90%;
            margin: 20px auto;
            border-collapse: collapse;
            background: black;
            border: 2px solid #ffffff;
            overflow-x: auto;
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
        }
        .manage-btn {
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            border: none;
            border-radius: 5px;
            color: black;
            font-weight: bold;
            transition: 0.3s;
        }
        .confirm-btn {
            background-color: #FFD700;
        }
        .confirm-btn:hover {
            background-color: #FFC107;
        }
        .status-confirmed {
            color: green;
            font-weight: bold;
        }
    </style>
</head>
<body>

<!-- Navigation Bar -->
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

<h2>Booking Management</h2>

<!-- Bookings Table -->
<table>
    <thead>
    <tr>
        <th>Order Number</th>
        <th>Customer Name</th>
        <th>Destination</th>
        <th>Scheduled Date</th>
        <th>Scheduled Time</th>
        <th>Car ID</th>
        <th>Driver ID</th>
        <th>Total Amount</th>
        <th>Status</th>
        <th>Confirmed By</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <%
        BookingDAO bookingDAO = BookingDAO.getInstance();
        List<Booking> bookings = bookingDAO.getAllBookings();

        for (Booking booking : bookings) {
    %>
    <tr>
        <td><%= booking.getOrderNumber() %></td>
        <td><%= booking.getCustomerName() %></td>
        <td><%= booking.getDestination() %></td>
        <td><%= booking.getScheduledDate().toString() %></td>
        <td><%= booking.getScheduledTime() %></td>
        <td><%= booking.getCarId() %></td>
        <td><%= booking.getDriverId() %></td>
        <td>$<%= booking.getTotalAmount() %></td>
        <td id="status_<%= booking.getBookingId() %>">
            <% if ("Confirmed".equals(booking.getStatus())) { %>
            <span class="status-confirmed">Confirmed</span>
            <% } else { %>
            <%= booking.getStatus() %>
            <% } %>
        </td>
        <td>
            <%= (booking.getConfirmedByEmployee() != null && !booking.getConfirmedByEmployee().isEmpty())
                    ? booking.getConfirmedByEmployee()
                    : "Not Confirmed" %>
        </td>
        <td>
            <% if ("Pending".equals(booking.getStatus())) { %>
            <button class="manage-btn confirm-btn confirm-booking-btn" data-booking-id="<%= booking.getBookingId() %>">Confirm</button>
            <% } else { %>
            <span class="status-confirmed">Confirmed</span>
            <% } %>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        let confirmBookingButtons = document.querySelectorAll(".confirm-booking-btn");

        confirmBookingButtons.forEach(button => {
            button.addEventListener("click", function () {
                let bookingId = this.dataset.bookingId;

                Swal.fire({
                    title: "Confirm Booking?",
                    text: "Are you sure you want to confirm this booking?",
                    icon: "warning",
                    showCancelButton: true,
                    confirmButtonText: "Yes, Confirm!",
                    cancelButtonText: "Cancel",
                    confirmButtonColor: "#28a745"
                }).then((result) => {
                    if (result.isConfirmed) {
                        fetch("../bookingConfirmation", {
                            method: "POST",
                            headers: { "Content-Type": "application/x-www-form-urlencoded" },
                            body: "booking_id=" + encodeURIComponent(bookingId)
                        })
                            .then(response => response.text())
                            .then(data => {
                                console.log("📌 Server Response:", data);
                                if (data.trim() === "success") {
                                    Swal.fire("Confirmed!", "The booking has been confirmed.", "success");
                                    document.getElementById(`status_${bookingId}`).innerHTML = "<span class='status-confirmed'>Confirmed</span>";
                                    document.querySelector(`button[data-booking-id="${bookingId}"]`).remove();
                                } else {
                                    Swal.fire("Error!", "Failed to confirm booking.", "error");
                                }
                            })
                            .catch(error => {
                                console.error("🚨 Booking Confirmation Failed:", error);
                                Swal.fire("Server Error!", "Please try again later.", "error");
                            });
                    }
                });
            });
        });
    });
</script>

</body>
</html>
