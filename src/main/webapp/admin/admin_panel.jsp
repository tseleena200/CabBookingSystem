<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.cabbookingsystem.model.User" %>

<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"admin".equals(user.getRole())) {
    response.sendRedirect("../html/login.html?error=unauthorized");
    return;
  }

  String success = request.getParameter("success");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Admin Panel</title>
  <link rel="stylesheet" href="../css/adminpanel.css">
  <script>
    // ✅ Panel Fade-in Animation
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

  <!-- ✅ Admin Actions -->
  <div class="admin-actions">
    <h3>Admin Actions</h3>
    <button onclick="location.href='register.html'"> Register Employee</button>
    <button onclick="location.href='add_driver.jsp'"> Add Driver</button>
    <button onclick="location.href='add_car.jsp'"> Add Car</button>
  </div>

  <!-- ✅ Show Success Messages Only -->
  <% if (success != null) { %>
  <p class="success-msg">
    ✅
    <% if ("employee_created".equals(success)) { %> Employee registered successfully! <% } %>
    <% if ("driver_added".equals(success)) { %> Driver added successfully! <% } %>
    <% if ("car_added".equals(success)) { %> Car added successfully! <% } %>
  </p>
  <% } %>

  <!-- ✅ Logout -->
  <a href="../html/login.html" class="logout-btn">Logout</a>
</div>

</body>
</html>
