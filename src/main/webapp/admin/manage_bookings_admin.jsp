<%@ page import="java.util.List, com.example.cabbookingsystem.dao.BookingDAO, com.example.cabbookingsystem.model.Booking, com.example.cabbookingsystem.dao.DriverDAO, com.example.cabbookingsystem.model.Driver" %>
<%@ page import="com.example.cabbookingsystem.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"admin".equals(user.getRole())) {
    response.sendRedirect("../html/login.html?error=unauthorized");
    return;
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Bookings | Admin</title>
  <link rel="stylesheet" href="../css/adminpanel.css">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <script src="../js/script.js" defer></script>
  <script>
    document.addEventListener("DOMContentLoaded", function () {
      document.getElementById("panel").style.opacity = "1";
      document.getElementById("panel").style.transform = "translateY(0)";
    });
  </script>
</head>
<body>

<div id="panel" style="opacity: 0; transform: translateY(-20px); transition: all 1s ease-out;">
  <h1>Manage Bookings - Admin Panel</h1>
  <h2>Welcome, <%= user.getFullName() %> (Admin)</h2>

  <input type="text" id="searchBooking" placeholder="Search by Order #, Customer, Driver...">
  <select id="filterStatus">
    <option value="">All Status</option>
    <option value="Pending">Pending</option>
    <option value="Confirmed">Confirmed</option>
    <option value="Completed">Completed</option>
    <option value="Cancelled">Cancelled</option>
  </select>

  <table>
    <thead>
    <tr>
      <th>Order #</th>
      <th>Customer Name</th>
      <th>Destination</th>
      <th>Scheduled Date</th>
      <th>Scheduled Time</th>
      <th>Driver</th>
      <th>Status</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <%
      BookingDAO bookingDAO = BookingDAO.getInstance();
      List<Booking> bookings = bookingDAO.getAllBookings();
      DriverDAO driverDAO = DriverDAO.getInstance();
      List<Driver> drivers = driverDAO.getAllDrivers();
      for (Booking booking : bookings) {
    %>
    <tr>
      <td><%= booking.getOrderNumber() %></td>
      <td><%= booking.getCustomerName() %></td>
      <td><%= booking.getDestination() %></td>
      <td><%= booking.getScheduledDate() %></td>
      <td><%= booking.getScheduledTime() %></td>
      <td>
        <select class="driver-select" data-order-number="<%= booking.getOrderNumber() %>">
          <option value="">Select Driver</option>
          <% for (Driver driver : drivers) { %>
          <option value="<%= driver.getId() %>" <%= booking.getDriverId() == driver.getId() ? "selected" : "" %>>
            <%= driver.getFullName() %>
          </option>
          <% } %>
        </select>
      </td>
      <td id="status_<%= booking.getOrderNumber() %>"><%= booking.getStatus() %></td>
      <td>
        <a href="<%= request.getContextPath() %>/jsp/editbooking.jsp?order_number=<%= booking.getOrderNumber() %>" class="edit-booking-btn">
           Edit
        </a>
        <button class="delete-booking-btn" data-order-number="<%= booking.getOrderNumber() %>"> Delete</button>
        <% if (!"Cancelled".equals(booking.getStatus())) { %>
        <button class="cancel-booking-btn" data-order-number="<%= booking.getOrderNumber() %>"> Cancel</button>
        <% } %>
        <% if ("Confirmed".equals(booking.getStatus())) { %>
        <button class="complete-booking-btn" data-order-number="<%= booking.getOrderNumber() %>">Mark as Completed</button>

        <% } %>
      </td>
    </tr>
    <% } %>
    </tbody>
  </table>
</div>


</body>
</html>
