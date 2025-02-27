<%@ page import="java.util.List, com.example.cabbookingsystem.dao.UserDAO, com.example.cabbookingsystem.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  // Fetch employee data from the database
  UserDAO userDAO = UserDAO.getInstance();
  List<User> employees = userDAO.getAllEmployees();
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Employees</title>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script> <!-- SweetAlert -->
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
    }

    th, td {
      padding: 12px;
      border: 2px solid #8f6a2a;
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
    }
  </style>
</head>
<body>

<h1>Manage Employees</h1>

<div class="table-container">
  <table>
    <thead>
    <tr>
      <th>ID</th>
      <th>Customer Registration Number</th>
      <th>Full Name</th>
      <th>Address</th>
      <th>NIC Number</th>
      <th>Email</th>
      <th>Role</th>
      <th>Actions</th> <!-- Added Actions column -->
    </tr>
    </thead>
    <tbody>
    <% for (User employee : employees) { %>
    <tr>
      <td><%= employee.getId() %></td>
      <td><%= employee.getCustomerRegistrationNumber() %></td>
      <td><%= employee.getFullName() %></td>
      <td><%= employee.getAddress() %></td>
      <td><%= employee.getNicNumber() %></td>
      <td><%= employee.getEmail() %></td>
      <td><%= employee.getRole() %></td>

      <!-- Action buttons for Edit and Delete -->
      <td>

        <!-- Edit Button with Full URL -->
        <a href="/CabBookingSystem_war_exploded/admin/edit_employee.jsp?customerRegistrationNumber=<%= employee.getCustomerRegistrationNumber() %>">Edit</a>




        <!-- Delete Button (Form) -->
        <!-- Delete Button with SweetAlert -->
        <form action="deleteEmployee" method="POST" id="deleteForm_<%= employee.getCustomerRegistrationNumber() %>">
          <input type="hidden" name="customerRegistrationNumber" value="<%= employee.getCustomerRegistrationNumber() %>">
          <button type="button" onclick="confirmDelete('<%= employee.getCustomerRegistrationNumber() %>')">Delete Employee</button>
        </form>

      </td>
    </tr>
    <% } %>
    </tbody>
  </table>
</div>

<!-- JavaScript for Delete functionality with SweetAlert -->
<script>
  // Function to confirm delete action for employee
  function confirmDelete(customerRegistrationNumber) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this employee!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
      if (result.isConfirmed) {
        // Submit the form to delete the employee
        document.getElementById('deleteForm_' + customerRegistrationNumber).submit();
        Swal.fire({
          title: 'Deleted!',
          text: 'Employee has been deleted successfully.',
          icon: 'success',
          confirmButtonColor: '#28a745',
          timer: 3000, // Show success for 2 seconds
          showConfirmButton: false // Hide the confirm button
        });
      } else {
        Swal.fire({
          title: 'Cancelled!',
          text: 'The action was cancelled.',
          icon: 'info',
          confirmButtonColor: '#3085d6',
          timer: 2000, // Show cancellation message for 2 seconds
          showConfirmButton: false // Hide the confirm button
        });
      }
    });
  }

  // Function to show success message after employee details edit
  function showEditSuccess() {
    Swal.fire({
      title: 'Updated!',
      text: 'Employee details have been updated successfully.',
      icon: 'success',
      confirmButtonColor: '#28a745',
      timer: 3000, // Show success for 2 seconds
      showConfirmButton: false // Hide the confirm button
    });
  }

  // Function to show error message if edit failed
  function showEditError() {
    Swal.fire({
      title: 'Error!',
      text: 'Something went wrong. Please try again.',
      icon: 'error',
      confirmButtonColor: '#d33',
      timer: 3000, // Show error for 2 seconds
      showConfirmButton: false // Hide the confirm button
    });
  }

  // Function to show error message for invalid employee data (for edit)
  function validateEmployeeEditForm() {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;

    // Validate name and email
    if (name.trim() === '' || email.trim() === '') {
      Swal.fire({
        title: 'Missing Information!',
        text: 'Please fill in all the required fields.',
        icon: 'warning',
        confirmButtonColor: '#f39c12',
        timer: 3000, // Show warning for 2 seconds
        showConfirmButton: false // Hide the confirm button
      });
      return false; // Prevent form submission
    }

    // Check if email format is valid
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      Swal.fire({
        title: 'Invalid Email!',
        text: 'Please enter a valid email address.',
        icon: 'error',
        confirmButtonColor: '#d33',
        timer: 3000, // Show error for 2 seconds
        showConfirmButton: false // Hide the confirm button
      });
      return false; // Prevent form submission
    }

    return true; // Form is valid
  }


</script>

</body>
</html>
