package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.service.CarService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/admin/DeleteCarServlet")
public class DeleteCarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CarService carService = new CarService(); // Using CarService

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String carIdStr = request.getParameter("car_id");

            if (carIdStr != null) {
                int carId = Integer.parseInt(carIdStr);

                // Use CarService to delete the car
                String result = carService.deleteCar(carId);

                // Handle the response based on the result from the service layer
                if ("success".equals(result)) {
                    response.sendRedirect(request.getContextPath() + "/admin/manage_cars_admin.jsp?success=car_deleted");
                } else {
                    response.sendRedirect("admin/manage_cars_admin.jsp?error=delete_failed");
                }
            } else {
                response.sendRedirect("admin/manage_cars_admin.jsp?error=missing_car_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin/manage_cars_admin.jsp?error=exception");
        }
    }

}
