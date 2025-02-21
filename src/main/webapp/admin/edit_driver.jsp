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
            background-color: #000; /* Black background for the page */
            color: white;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .content {
            max-width: 700px; /* Set a standard width */
            width: 100%;
            background-color: #222; /* Dark grey background for form container */
            padding: 30px;
            border-radius: 12px; /* Rounded corners */
            box-shadow: 0 4px 10px rgba(255, 255, 255, 0.1); /* Subtle white glow */
            text-align: left;
        }

        h2 {
            font-size: 28px;
            font-weight: bold;
            color: #b58a3e; /* Dark gold title */
            margin-bottom: 20px;
            text-align: center;
        }

        label {
            font-size: 16px;
            color: #ccc; /* Lighter grey text for labels */
            margin-bottom: 8px;
            display: block;
            text-align: center; /* Centers label text */
        }

        input {
            width: 60%; /* Reduced width to make it more compact */
            padding: 12px;
            margin: 10px auto; /* Centers the input fields */
            border-radius: 5px;
            border: 1px solid #444; /* Dark border */
            background-color: #333; /* Darker input background */
            color: white;
            font-size: 16px;
            transition: 0.3s;
            display: block; /* Ensures full block layout */
        }
        input:focus {
            border-color: #b58a3e; /* Gold border on focus */
            box-shadow: 0 0 10px rgba(181, 138, 62, 0.5); /* Soft glow effect */
            outline: none;
        }

        button {
            background-color: #b58a3e; /* Dark gold background */
            color: black;
            font-weight: bold;
            padding: 12px 20px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            width: 40%; /* Matches input width */
            font-size: 16px;
            margin: 20px auto 0; /* Centers the button */
            display: block; /* Ensures it takes full width and centers */
            transition: 0.3s;
        }

        button:hover {
            background-color: #9c7532; /* Slightly darker gold on hover */
        }

        button:active {
            background-color: #7a5b2b; /* Darker gold when clicked */
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .content {
                width: 90%;
                padding: 20px;
            }
        }
    </style>

</head>
<body>

<div class="content">  <!-- ✅ Dark Grey Container for Form -->
    <h2>Edit Driver</h2>

    <form action="EditDriverServlet" method="post">
        <input type="hidden" name="driver_id" value="<%= driver.getId() %>">

        <label for="full_name">Full Name:</label>
        <input type="text" name="full_name" id="full_name" value="<%= driver.getFullName() %>" required>

        <label for="contact_number">Phone Number:</label>
        <input type="text" name="contact_number" id="contact_number" value="<%= driver.getContactNumber() %>" required>

        <label for="license_number">License Number:</label>
        <input type="text" name="license_number" id="license_number" value="<%= driver.getLicenseNumber() %>" required>

        <button type="submit">Save Changes</button>
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
