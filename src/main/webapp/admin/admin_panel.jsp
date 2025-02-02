<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.cabbookingsystem.model.User" %>

<%
  User user = (User) session.getAttribute("user");

  //  Redirect if not logged in or not an admin
  if (user == null || !"admin".equals(user.getRole())) {
    response.sendRedirect("../html/login.html?error=unauthorized");
    return;
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Panel - CityCab</title>
  <link rel="stylesheet" href="../css/style.css"> <!-- ✅ Link CSS -->
</head>
<body>

<!--  Navbar -->
<div class="navbar">
  <h2>Admin Panel - CityCab</h2>
  <a href="http://localhost:8080/CabBookingSystem_war_exploded/html/login.html" class="logout-btn">Logout</a>
</div>

<!--  Admin Dashboard -->
<div class="container">
  <h2>Welcome, <%= user.getFullName() %> (Admin)</h2>

  <!-- Admin Actions -->
  <div class="admin-actions">
    <h3>Admin Actions</h3>
    <a href="register.html" class="btn">Register Employee</a>
  </div>

  <!--  Success/Error Message Handling -->
  <% String success = request.getParameter("success"); %>
  <% if ("employee_created".equals(success)) { %>
  <p class="success-msg">✅ Employee registered successfully!</p>
  <% } %>

  <% String error = request.getParameter("error"); %>
  <% if ("db_insert_fail".equals(error)) { %>
  <p class="error-msg">❌ Registration failed. Try again.</p>
  <% } %>
</div>

</body>
</html>
