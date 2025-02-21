<%@ page import="com.example.cabbookingsystem.model.User" %>
<%@ page import="com.example.cabbookingsystem.dao.UserDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Edit Employee</title>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script> <!-- SweetAlert -->
  <style>
    /* Basic styling for form and layout */
    body {
      font-family: Arial, sans-serif;
      background-color: #1a1a1a;
      color: white;
      padding: 30px;
    }
    h1 {
      color: #b58a3e;
      text-align: center;
      font-size: 28px;
    }
    form {
      background-color: #333;
      padding: 20px;
      margin: auto;
      max-width: 500px;
      border-radius: 8px;
    }
    input[type="text"], input[type="email"] {
      width: 100%;
      padding: 10px;
      margin: 10px 0;
      background-color: #2a2a2a;
      color: white;
      border: 1px solid #8f6a2a;
      border-radius: 4px;
    }
    button {
      background-color: #b58a3e;
      color: black;
      border: none;
      padding: 10px 20px;
      font-size: 18px;
      font-weight: bold;
      border-radius: 8px;
      cursor: pointer;
      transition: 0.3s;
    }
    button:hover {
      background-color: #9c7532;
    }
  </style>
</head>
<body>

<h1>Edit Employee Details</h1>

<%
  String customerRegistrationNumber = request.getParameter("customerRegistrationNumber");
  // Fetch the employee details using the registration number
  UserDAO userDAO = UserDAO.getInstance();
  User employee = userDAO.getEmployeeByCustomerRegistrationNumber(customerRegistrationNumber);
  if (employee == null) {
    response.sendRedirect("manage_employees.jsp");
    return;
  }
%>

<form action="/CabBookingSystem_war_exploded/admin/updateEmployee" method="POST">
  <input type="hidden" name="customerRegistrationNumber" value="<%= employee.getCustomerRegistrationNumber() %>" />

  <label for="fullName">Full Name:</label>
  <input type="text" id="fullName" name="fullName" value="<%= employee.getFullName() %>" required />

  <label for="address">Address:</label>
  <input type="text" id="address" name="address" value="<%= employee.getAddress() %>" required />

  <label for="nicNumber">NIC Number:</label>
  <input type="text" id="nicNumber" name="nicNumber" value="<%= employee.getNicNumber() %>" required />

  <label for="email">Email:</label>
  <input type="email" id="email" name="email" value="<%= employee.getEmail() %>" required />

  <button type="submit">Update Employee</button>
</form>


<script>
  // Optional: SweetAlert for success/failure after form submission
  <% if (request.getAttribute("updateStatus") != null) { %>
  let status = "<%= request.getAttribute("updateStatus") %>";
  if (status === "success") {
    Swal.fire({
      title: 'Success!',
      text: 'Employee details updated successfully.',
      icon: 'success',
      confirmButtonText: 'OK'
    }).then(() => {
      window.location.href = 'manageEmployees.jsp';  // Redirect after success
    });
  } else if (status === "failure") {
    Swal.fire({
      title: 'Error!',
      text: 'Failed to update employee details.',
      icon: 'error',
      confirmButtonText: 'OK'
    }).then(() => {
      window.location.href = 'manageEmployees.jsp';  // Redirect after failure
    });
  }
  <% } %>
</script>

</body>
</html>
