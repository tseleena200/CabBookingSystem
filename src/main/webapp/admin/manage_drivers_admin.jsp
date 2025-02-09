<%@ page import="java.util.List, com.example.cabbookingsystem.dao.DriverDAO, com.example.cabbookingsystem.model.Driver" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  if (session.getAttribute("user_role") == null || !session.getAttribute("user_role").equals("admin")) {
    response.sendRedirect("../html/login.html?error=unauthorized");
    return;
  }
%>

<!DOCTYPE html>
<html>
<head>

  <a href="admin_panel.jsp" class="back-arrow">←</a>


  <title>Manage Drivers | Mega CityCab</title>
  <link rel="stylesheet" href="../css/homepage.css">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

  <style>
    body {
      font-family: Arial, sans-serif;
      text-align: center;
      background-color: #000;
      color: white;
      margin: 0;
      padding: 0;
    }

    .content {
      padding-top: 100px;
      max-width: 90%;
      margin: auto;
    }

    h2 {
      font-size: 26px;
      font-weight: bold;
      margin-bottom: 20px;
    }

    .scrollable-table {
      max-height: 400px;
      overflow-y: auto;
      margin-top: 20px;
      border: 2px solid #ffffff;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      background: black;
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
      position: sticky;
      top: 0;
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

    .add-driver-btn {
      background-color: #FFD700;
      margin-bottom: 20px;
    }

    .edit-btn {
      background-color: #FFA500;
    }

    .delete-btn {
      background-color: #FF4500;
      color: white;
    }

    .manage-btn:hover {
      opacity: 0.8;
    }
    .back-arrow {
      position: absolute;
      top: 20px;
      left: 20px;
      font-size: 30px;
      color: white;
      text-decoration: none;
      font-weight: bold;
    }

    .back-arrow:hover {
      opacity: 0.7;
    }

  </style>
</head>
<body>

<div class="content">
  <h2> Manage Drivers</h2>


  <button class="manage-btn add-driver-btn" onclick="location.href='add_driver.jsp'">+ Add New Driver</button>

  <!--  Drivers Table -->
  <div class="scrollable-table">
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
      <%
        DriverDAO driverDAO = DriverDAO.getInstance();
        List<Driver> drivers = driverDAO.getAllDrivers();
        for (Driver driver : drivers) {
      %>
      <tr>
        <td><%= driver.getFullName() %></td>
        <td><%= driver.getContactNumber() %></td>
        <td><%= driver.getLicenseNumber() %></td>
        <td>
          <button class="manage-btn edit-btn" onclick="editDriver(<%= driver.getId() %>)">Edit</button>
          <button class="manage-btn delete-btn" onclick="deleteDriver(<%= driver.getId() %>)">Delete</button>
        </td>
      </tr>
      <%
        }
      %>
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


  const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.has('success')) {
    let successMessage = "";
    if (urlParams.get('success') === "driver_updated") {
      successMessage = "Driver details updated successfully!";
    } else if (urlParams.get('success') === "driver_deleted") {
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
  }
</script>

</body>
</html>
