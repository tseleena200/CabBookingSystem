<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.example.cabbookingsystem.dao.CarDAO, com.example.cabbookingsystem.model.Car" %>

<%
  // Admin session check
  if (session.getAttribute("user_role") == null || !session.getAttribute("user_role").equals("admin")) {
    response.sendRedirect("../html/login.html?error=unauthorized");
    return;
  }

  // Check for success or error messages
  String successMessage = request.getParameter("success");
  String errorMessage = request.getParameter("error");

  // Fetch car data
  CarDAO carDAO = CarDAO.getInstance();
  List<Car> cars = carDAO.getAllCars();
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Cars | Mega CityCab</title>
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
    .add-car-container {
      display: flex;
      justify-content: center;
      margin-bottom: 20px;
    }

    .add-car-btn {
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

    .add-car-btn:hover {
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

      .add-car-btn {
        font-size: 16px;
        padding: 12px 24px;
        width: 90%;
      }
    }
  </style>
</head>
<body>

<a href="admin_panel.jsp" class="back-arrow">←</a>

<!-- SweetAlert Notifications -->
<script>
  document.addEventListener("DOMContentLoaded", function () {
    <% if ("car_updated".equals(successMessage)) { %>
    Swal.fire({
      title: "Success!",
      text: "Car details updated successfully.",
      icon: "success",
      confirmButtonColor: "#28a745"
    });
    <% } else if ("update_failed".equals(errorMessage)) { %>
    Swal.fire({
      title: "Error!",
      text: "Failed to update car. Try again.",
      icon: "error",
      confirmButtonColor: "#d33"
    });
    <% } else if ("server_error".equals(errorMessage)) { %>
    Swal.fire({
      title: "Server Error!",
      text: "An unexpected error occurred.",
      icon: "error",
      confirmButtonColor: "#d33"
    });
    <% } %>
  });
</script>

<div class="content">
  <h2>Manage Cars</h2>

  <!-- Add Car Button -->
  <div class="add-car-container">
    <button class="add-car-btn" onclick="location.href='add_car.jsp'">+ Add New Car</button>
  </div>

  <!-- Cars Table -->
  <div class="table-container">
    <table>
      <thead>
      <tr>
        <th>Car Model</th>
        <th>License Plate</th>
        <th>Capacity</th>
        <th>Driver ID</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <% for (Car car : cars) { %>
      <tr>
        <td><%= car.getModel() %></td>
        <td><%= car.getLicensePlate() %></td>
        <td><%= car.getCapacity() %></td>
        <td><%= car.getDriverId() == null ? "Unassigned" : car.getDriverId() %></td>
        <td>
          <button class="manage-btn edit-btn" onclick="editCar(<%= car.getId() %>)">Edit</button>
          <button class="manage-btn delete-btn" onclick="deleteCar(<%= car.getId() %>)">Delete</button>
        </td>
      </tr>
      <% } %>
      </tbody>
    </table>
  </div>
</div>

<script>
  function editCar(carId) {
    window.location.href = 'edit_car.jsp?car_id=' + carId;
  }

  function deleteCar(carId) {
    Swal.fire({
      title: "Are you sure?",
      text: "This car will be permanently deleted!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "Cancel",
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6"
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.href = 'DeleteCarServlet?car_id=' + carId;
      }
    });
  }
</script>

</body>
</html>
