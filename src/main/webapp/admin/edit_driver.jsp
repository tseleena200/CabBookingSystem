<%@ page import="com.example.cabbookingsystem.dao.DriverDAO, com.example.cabbookingsystem.model.Driver" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    if (session.getAttribute("user_role") == null || !session.getAttribute("user_role").equals("admin")) {
        response.sendRedirect("../html/login.html?error=unauthorized");
        return;
    }

    String driverIdStr = request.getParameter("driver_id");
    Driver driver = null;

    if (driverIdStr != null) {
        try {
            int driverId = Integer.parseInt(driverIdStr);
            driver = DriverDAO.getInstance().getDriverById(driverId);
        } catch (NumberFormatException e) {
            response.sendRedirect("manage_drivers_admin.jsp?error=invalid_driver_id");
            return;
        }
    }

    if (driver == null) {
        response.sendRedirect("manage_drivers_admin.jsp?error=driver_not_found");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Driver | Mega CityCab</title>
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

        form {
            background: black;
            padding: 20px;
            border-radius: 10px;
            display: inline-block;
            width: 50%;
        }

        input {
            width: 90%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid white;
            background: black;
            color: white;
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
            margin-top: 10px;
        }

        .save-btn {
            background-color: #FFD700;
        }

        .manage-btn:hover {
            opacity: 0.8;
        }
    </style>
</head>
<body>

<div class="content">
    <h2>Edit Driver</h2>

    <form action="EditDriverServlet" method="post">
        <input type="hidden" name="driver_id" value="<%= driver.getId() %>">

        <label for="full_name">Full Name:</label>
        <input type="text" name="full_name" id="full_name" value="<%= driver.getFullName() %>" required>

        <label for="contact_number">Phone Number:</label>
        <input type="text" name="contact_number" id="contact_number" value="<%= driver.getContactNumber() %>" required>

        <label for="license_number">License Number:</label>
        <input type="text" name="license_number" id="license_number" value="<%= driver.getLicenseNumber() %>" required>

        <button type="submit" class="manage-btn save-btn">Save Changes</button>

    </form>
</div>

<script>
    //  Show SweetAlert if driver is updated
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('success')) {
        Swal.fire({
            title: "Success!",
            text: "Driver updated successfully.",
            icon: "success",
            confirmButtonColor: "#FFD700"
        }).then(() => {
            // ✅ Redirect Automatically to Manage Drivers after Confirmation
            window.location.href = "manage_drivers_admin.jsp";
        });
    }
</script>

</body>
</html>
