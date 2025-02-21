package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.DriverDAO;
import com.example.cabbookingsystem.model.Driver;

import com.example.cabbookingsystem.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addDriver")
public class AddDriverServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("full_name");
        String licenseNumber = request.getParameter("license_number");
        String contactNumber = request.getParameter("contact_number");

        //  Validate input
        if (fullName == null || licenseNumber == null || contactNumber == null || fullName.isEmpty() || licenseNumber.isEmpty() || contactNumber.isEmpty()) {
            response.getWriter().write("missing_fields");
            return;
        }

        //  Use service layer to handle the addition
        DriverService driverService = new DriverService();
        String result = driverService.addDriver(fullName, licenseNumber, contactNumber);

        response.getWriter().write(result);
    }
}

