<%@ page import="java.util.List, com.example.cabbookingsystem.dao.DriverDAO, com.example.cabbookingsystem.model.Driver" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  // Admin session check
  if (session.getAttribute("user_role") == null || !session.getAttribute("user_role").equals("admin")) {
    response.sendRedirect("../html/login.html?error=unauthorized");
    return;
  }

  // Fetch driver data
  DriverDAO driverDAO = DriverDAO.getInstance();
  List<Driver> drivers = driverDAO.getAllDrivers();
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Drivers | Mega CityCab</title>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script> <!-- ✅ SweetAlert -->

  <style>
    /* Global Styles */
    body {
      font-family: Arial, sans-serif;
      text-align: center;
      background-color: #1a1a1a;
      color: white;
      margin: 0;
      padding: 0;
    }

    .content {
      padding-top: 50px;
      max-width: 85%;
      margin: auto;
    }

    h2 {
      font-size: 28px;
      font-weight: bold;
      color: #b58a3e;
    }

    /* Centered Add Button */
    .add-driver-container {
      display: flex;
      justify-content: center;
      margin-bottom: 20px;
    }

    .add-driver-btn {
      background-color: #b58a3e;
      color: black;
      border: none;
      padding: 15px 30px;
      font-size: 18px;
      font-weight: bold;
      border-radius: 8px;
      cursor: pointer;
      transition: 0.3s;
      width: 300px;
      text-align: center;
      box-shadow: 0px 4px 10px rgba(255, 255, 255, 0.1);
    }

    .add-driver-btn:hover {
      background-color: #9c7532;
    }

    /* Table Container */
    .table-container {
      background-color: #333;
      padding: 20px;
      border-radius: 12px;
      box-shadow: 0 4px 10px rgba(255, 255, 255, 0.1);
      display: flex;
      justify-content: center;
      margin-top: 20px;
    }

    table {
      width: 100%;
      max-width: 1000px;
      border-collapse: collapse;
      background: #222;
      border-radius: 12px;
      overflow: hidden;
    }

    th, td {
      padding: 12px;
      border: 1px solid #8f6a2a;
      text-align: center;
      font-size: 16px;
    }

    th {
      background-color: #8f6a2a;
      color: white;
      font-weight: bold;
      position: sticky;
      top: 0;
    }

    td {
      background-color: #2a2a2a;
      color: white;
    }

    /* Buttons */
    .manage-btn {
      padding: 10px 20px;
      font-size: 16px;
      cursor: pointer;
      border: none;
      border-radius: 6px;
      font-weight: bold;
      transition: 0.3s;
      width: 100px;
    }

    .edit-btn {
      background-color: #c7923e;
      color: black;
    }

    .delete-btn {
      background-color: #a40000;
      color: white;
    }

    .manage-btn:hover {
      opacity: 0.85;
      transform: scale(1.05);
    }

    /* Back Button */
    .back-arrow {
      position: absolute;
      top: 20px;
      left: 20px;
      font-size: 30px;
      color: #b58a3e;
      text-decoration: none;
      font-weight: bold;
      transition: 0.3s;
    }

    .back-arrow:hover {
      opacity: 0.7;
      transform: scale(1.1);
    }

    /* Responsive Design */
    @media (max-width: 768px) {
      .content {
        max-width: 95%;
      }

      table {
        font-size: 14px;
      }

      th, td {
        padding: 10px;
      }

      .manage-btn {
        font-size: 14px;
        padding: 8px 16px;
      }

      .add-driver-btn {
        font-size: 16px;
        padding: 12px 24px;
        width: 90%;
      }
    }
  </style>
</head>
<body>

<a href="admin_panel.jsp" class="back-arrow">←</a>

<div class="content">
  <h2>Manage Drivers</h2>

  <!-- Add Driver Button -->
  <div class="add-driver-container">
    <button class="add-driver-btn" onclick="location.href='add_driver.jsp'">+ Add New Driver</button>
  </div>

  <!-- Drivers Table -->
  <div class="table-container">
    <table>
      <thead>
      <tr>
        <th>Driver Name</th>
        <th>Phone Number</th>
        <th>License Number</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <% for (Driver driver : drivers) { %>
      <tr>
        <td><%= driver.getFullName() %></td>
        <td><%= driver.getContactNumber() %></td>
        <td><%= driver.getLicenseNumber() %></td>
        <td>
          <button class="manage-btn edit-btn" onclick="editDriver(<%= driver.getId() %>)">Edit</button>
          <button class="manage-btn delete-btn" onclick="deleteDriver(<%= driver.getId() %>)">Delete</button>
        </td>
      </tr>
      <% } %>
      </tbody>
    </table>
  </div>
</div>

<script>
  function editDriver(driverId) {
    window.location.href = 'edit_driver.jsp?driver_id=' + driverId;
  }

  function deleteDriver(driverId) {
    Swal.fire({
      title: "Are you sure?",
      text: "This driver will be permanently deleted!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "Cancel",
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6"
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.href = 'DeleteDriverServlet?driver_id=' + driverId;
      }
    });
  }

  document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    let successMessage = "";

    if (urlParams.get("success") === "driver_updated") {
      successMessage = "Driver details updated successfully!";
    } else if (urlParams.get("success") === "driver_deleted") {
      successMessage = "Driver deleted successfully!";
    }

    if (successMessage) {
      Swal.fire({
        title: "Success!",
        text: successMessage,
        icon: "success",
        confirmButtonColor: "#28a745"
      });
    }
  });
</script>

</body>
</html>
