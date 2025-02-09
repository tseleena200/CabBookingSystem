package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.CarDAO;
import com.example.cabbookingsystem.model.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/EditCarServlet")
public class EditCarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CarDAO carDAO = CarDAO.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //  Fetch car_id safely
            String carIdParam = request.getParameter("car_id");
            if (carIdParam == null || carIdParam.trim().isEmpty()) {
                response.sendRedirect("admin/manage_cars_admin.jsp?error=missing_car_id");
                return;
            }
            int carId = Integer.parseInt(carIdParam);

            //  Fetch and sanitize form inputs
            String model = request.getParameter("model");
            String licensePlate = request.getParameter("license_plate");
            int capacity = Integer.parseInt(request.getParameter("capacity"));

            //  Handle optional driver_id (can be NULL)
            String driverIdParam = request.getParameter("driver_id");
            Integer driverId = (driverIdParam != null && !driverIdParam.trim().isEmpty()) ? Integer.parseInt(driverIdParam) : null;

            //  Create a Car object with updated values
            Car car = new Car(model, licensePlate, capacity, driverId);
            car.setId(carId);

            //  Update car in the database
            boolean updated = carDAO.updateCar(car);
            if (updated) {
                System.out.println(" Car updated successfully.");
                response.sendRedirect("admin/manage_cars_admin.jsp?success=car_updated");

            } else {
                System.out.println(" Car update failed.");
                response.sendRedirect("admin/manage_cars_admin.jsp?error=update_failed");
            }
        } catch (Exception e) {
            System.out.println(" Error in EditCarServlet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("admin/manage_cars_admin.jsp?error=server_error");
        }
    }
}
