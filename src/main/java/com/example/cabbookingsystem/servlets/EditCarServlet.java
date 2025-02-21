package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.service.CarService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/EditCarServlet")
public class EditCarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CarService carService = new CarService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String carIdParam = request.getParameter("car_id");
            if (carIdParam == null || carIdParam.trim().isEmpty()) {
                response.sendRedirect("admin/manage_cars_admin.jsp?error=missing_car_id");
                return;
            }

            int carId = Integer.parseInt(carIdParam);
            String model = request.getParameter("model");
            String licensePlate = request.getParameter("license_plate");
            int capacity = Integer.parseInt(request.getParameter("capacity"));

            String driverIdParam = request.getParameter("driver_id");
            Integer driverId = (driverIdParam != null && !driverIdParam.trim().isEmpty()) ? Integer.parseInt(driverIdParam) : null;

            // Call the service layer to update the car
            String result = carService.updateCar(carId, model, licensePlate, capacity, driverId);

            if ("success".equals(result)) {
                response.sendRedirect("admin/manage_cars_admin.jsp?success=car_updated");
            } else {
                response.sendRedirect("admin/manage_cars_admin.jsp?error=update_failed");
            }
        } catch (Exception e) {
            response.sendRedirect("admin/manage_cars_admin.jsp?error=server_error");
        }
    }
}
