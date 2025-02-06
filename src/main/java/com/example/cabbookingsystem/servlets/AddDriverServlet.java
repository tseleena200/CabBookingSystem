package com.example.cabbookingsystem.servlets;

import com.example.cabbookingsystem.dao.DriverDAO;
import com.example.cabbookingsystem.model.Driver;

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

        //  Check if the license number already exists
        if (DriverDAO.getInstance().isDuplicateLicense(licenseNumber)) {
            response.getWriter().write("duplicate_license");
            return;
        }

        Driver newDriver = new Driver(fullName, licenseNumber, contactNumber);
        boolean success = DriverDAO.getInstance().addDriver(newDriver);

        if (success) {
            response.getWriter().write("success");
        } else {
            response.getWriter().write("db_insert_fail");
        }
    }
}
