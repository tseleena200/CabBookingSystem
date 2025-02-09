package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/DeleteCarServlet")
public class DeleteCarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CarDAO carDAO = CarDAO.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String carIdStr = request.getParameter("car_id");

            if (carIdStr != null) {
                int carId = Integer.parseInt(carIdStr);

                //  Delete Car from Database
                boolean deleted = carDAO.deleteCar(carId);

                if (deleted) {
                    response.sendRedirect("admin/manage_cars_admin.jsp?success=car_deleted");
                } else {
                    response.sendRedirect("admin/manage_cars_admin.jsp?error=delete_failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin/manage_cars_admin.jsp?error=exception");
        }
    }
}
