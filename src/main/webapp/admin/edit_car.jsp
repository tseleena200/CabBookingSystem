<%@ page import="com.example.cabbookingsystem.dao.CarDAO, com.example.cabbookingsystem.model.Car" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //  Check if the user is an admin
    if (session.getAttribute("user_role") == null || !session.getAttribute("user_role").equals("admin")) {
        response.sendRedirect("../html/login.html?error=unauthorized");
        return;
    }

    //  Get car ID from request
    String carIdParam = request.getParameter("car_id");
    if (carIdParam == null) {
        response.sendRedirect("manage_cars_admin.jsp?error=invalid_car_id");
        return;
    }

    int carId = Integer.parseInt(carIdParam);
    CarDAO carDAO = CarDAO.getInstance();
    Car car = carDAO.getCarById(carId);

    if (car == null) {
        response.sendRedirect("manage_cars_admin.jsp?error=car_not_found");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Car | Mega CityCab</title>
    <link rel="stylesheet" href="../css/homepage.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #1a1a1a; /* Darker background for contrast */
            color: white;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .content {
            max-width: 700px;
            width: 100%;
            background-color: #333; /* Dark background for form container */
            padding: 40px; /* Increased padding for a larger form */
            border-radius: 10px; /* Rounded corners */
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.5); /* Subtle shadow for depth */
            text-align: left;
            height: 500px; /* Fixed height for longer form */
        }

        h2 {
            font-size: 28px;
            font-weight: bold;
            color: #b58a3e; /* Dark gold for header */
            margin-bottom: 20px;
            text-align: center;
        }

        label {
            font-size: 16px;
            color: #ccc; /* Lighter color for labels */
            margin-bottom: 8px;
            display: block;
        }

        input, select {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border-radius: 5px;
            border: 1px solid #444; /* Dark border */
            background-color: #222; /* Darker input background */
            color: white;
            font-size: 16px;
            transition: 0.3s;
        }

        input:focus, select:focus {
            border-color: #b58a3e; /* Dark gold border on focus */
            box-shadow: 0 0 10px rgba(181, 138, 62, 0.5); /* Soft glow effect */
            outline: none;
        }

        .submit-btn {
            background-color: #b58a3e; /* Dark gold background */
            color: black;
            font-weight: bold;
            padding: 12px 20px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            width: 100%;
            font-size: 16px;
            margin-top: 20px;
            transition: 0.3s;
        }

        .submit-btn:hover {
            background-color: #9c7532; /* Slightly darker gold on hover */
        }

        .submit-btn:active {
            background-color: #7a5b2b; /* Darker gold when clicked */
        }

        /* For smaller screen sizes */
        @media (max-width: 768px) {
            .content {
                width: 90%;
                padding: 20px;
            }
        }
    </style>



</head>
<body>

<div class="content">
    <h2>Edit Car</h2>
    <form action="../EditCarServlet" method="post">
        <input type="hidden" name="car_id" value="<%= car.getId() %>">  <!-- ✅ Ensure the name is 'car_id' -->

        <label>Car Model:</label>
        <input type="text" name="model" value="<%= car.getModel() %>" required><br>

        <label>License Plate:</label>
        <input type="text" name="license_plate" value="<%= car.getLicensePlate() %>" required><br>

        <label>Capacity:</label>
        <input type="number" name="capacity" value="<%= car.getCapacity() %>" required><br>

        <label>Driver ID (Optional):</label>
        <input type="text" name="driver_id" value="<%= car.getDriverId() != null ? car.getDriverId() : "" %>"><br>

        <button type="submit" class="submit-btn">Update Car</button>
    </form>
</div>

</body>
</html>
