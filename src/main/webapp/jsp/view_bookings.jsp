<%@ page import="java.util.List, com.example.cabbookingsystem.dao.BookingDAO, com.example.cabbookingsystem.model.Booking" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Bookings | Mega CityCab</title>
    <link rel="stylesheet" href="../css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<h2>Booking Management</h2>

<table border="1">
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
        <td><%= (booking.getConfirmedByEmployee() != null && !booking.getConfirmedByEmployee().isEmpty())
                ? booking.getConfirmedByEmployee()
                : "Not Confirmed" %></td>

        <% if ("Pending".equals(booking.getStatus())) { %>
            <button class="confirm-booking-btn" data-booking-id="<%= booking.getBookingId() %>">Confirm</button>
            <% } else { %>
            <span style="color: green; font-weight: bold;">Confirmed</span>
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
                                console.log(" Server Response:", data);
                                if (data.trim() === "success") {
                                    Swal.fire("Confirmed!", "The booking has been confirmed.", "success");
                                    document.getElementById(`status_${bookingId}`).innerText = "Confirmed";
                                    document.querySelector(`button[data-booking-id="${bookingId}"]`).remove();
                                } else {
                                    Swal.fire("Error!", "Failed to confirm booking.", "error");
                                }
                            })
                            .catch(error => {
                                console.error(" Booking Confirmation Failed:", error);
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
