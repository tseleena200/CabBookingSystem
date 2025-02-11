<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.example.cabbookingsystem.dao.CarDAO, com.example.cabbookingsystem.model.Car" %>
<%
  // Admin session check
  if (session.getAttribute("user_role") == null || !session.getAttribute("user_role").equals("admin")) {
    response.sendRedirect("../html/login.html?error=unauthorized");
    return;
  }

  //  Check for success or error messages
  String successMessage = request.getParameter("success");
  String errorMessage = request.getParameter("error");
%>

<!DOCTYPE html>
<html>
<head>

  <a href="admin_panel.jsp" class="back-arrow">←</a>
  <title>Manage Cars | Mega CityCab</title>
  <link rel="stylesheet" href="../css/homepage.css">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script> <!-- ✅ SweetAlert -->

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
    .add-car-btn {
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
  <h2> Manage Cars</h2>

  <!--  Add Car Button -->
  <button class="manage-btn add-car-btn" onclick="location.href='add_car.jsp'">+ Add New Car</button>

  <!-- Cars Table -->
  <div class="scrollable-table">
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
      <%
        CarDAO carDAO = CarDAO.getInstance();
        List<Car> cars = carDAO.getAllCars();
        for (Car car : cars) {
      %>
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
      <%
        }
      %>
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
