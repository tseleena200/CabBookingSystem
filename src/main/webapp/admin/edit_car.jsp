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
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #000;
            color: white;
            margin: 0;
            padding: 0;
        }

        .content {
            padding-top: 100px;
            max-width: 50%;
            margin: auto;
            background-color: #222;
            padding: 20px;
            border-radius: 10px;
        }

        h2 {
            font-size: 26px;
            font-weight: bold;
        }

        input, select {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
            border: 1px solid #fff;
            background-color: black;
            color: white;
        }

        .submit-btn {
            background-color: #FFD700;
            color: black;
            font-weight: bold;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 10px;
            transition: 0.3s;
        }

        .submit-btn:hover {
            opacity: 0.8;
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

        <button type="submit">Update Car</button>
    </form>
</div>

</body>
</html>
