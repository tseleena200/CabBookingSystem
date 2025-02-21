<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.cabbookingsystem.model.User" %>

<%
  // Retrieve the User object from the session
  User user = (User) session.getAttribute("user");

  // If user is not logged in or role is not admin, redirect to login
  if (user == null || !"admin".equals(user.getRole())) {
    response.sendRedirect("../html/login.html?error=unauthorized");
    return;
  }

  // Get success message if any
  String success = request.getParameter("success");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Admin Panel | Mega CityCab</title>
  <link rel="stylesheet" href="../css/adminpanel.css">
  <script>
    document.addEventListener("DOMContentLoaded", function () {
      document.getElementById("panel").style.opacity = "1";
      document.getElementById("panel").style.transform = "translateY(0)";
    });
  </script>
</head>
<body>

<div id="panel" style="opacity: 0; transform: translateY(-20px); transition: all 1s ease-out;">
  <h1>Admin Panel - CityCab</h1>
  <h2>Welcome, <%= user.getFullName() %> (Admin)</h2>

  <div class="admin-actions">
    <h3>Admin Actions</h3>
    <button onclick="location.href='register.html'">Register Employee</button>
    <button onclick="location.href='../admin/manage_drivers_admin.jsp'">Manage Drivers</button>
    <button onclick="location.href='../admin/manage_cars_admin.jsp'">Manage Cars</button>
    <button onclick="location.href='../admin/manage_bookings_admin.jsp'">Manage Bookings</button>
    <!-- ✅ NEW -->
    <button onclick="location.href='../admin/manage_employees.jsp'">Manage Employees</button> <!-- 🔹 Placeholder -->
  </div>

  <% if (success != null) { %>
  <p class="success-msg">
    ✅
    <% if ("employee_created".equals(success)) { %> Employee registered successfully! <% } %>
    <% if ("driver_added".equals(success)) { %> Driver added successfully! <% } %>
    <% if ("car_added".equals(success)) { %> Car added successfully! <% } %>
  </p>
  <% } %>

  <!-- ✅ Logout -->
  <a href="../html/login.html" class="logout-btn" onclick="session.invalidate();">Logout</a>
</div>

</body>
</html>
