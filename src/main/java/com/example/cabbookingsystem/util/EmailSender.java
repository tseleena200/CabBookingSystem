package com.example.cabbookingsystem.util;

import com.example.cabbookingsystem.dao.BookingDAO;
import com.example.cabbookingsystem.model.Booking;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    private static final String SENDER_EMAIL = "thenuriseleena@gmail.com";
    private static final String SENDER_PASSWORD = "cpzzvtdtbxyyaoyh";

    public static void sendBillEmail(Booking booking) {

        BookingDAO bookingDAO = BookingDAO.getInstance();
        String driverName = bookingDAO.getDriverNameById(booking.getDriverId());
        String carLicensePlate = bookingDAO.getCarLicensePlateById(booking.getCarId());

        //  Configure SMTP properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        //  Authenticate
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(booking.getCustomerEmail()));
            message.setSubject("Your Cab Booking Bill - Order #" + booking.getOrderNumber());

            //  Email Body
            String emailBody = "Dear " + booking.getCustomerName() + ",\n\n" +
                    "Thank you for choosing MegaCityCab! Here are your complete booking details:\n\n" +
                    "** Booking Information**\n" +
                    " Order Number: " + booking.getOrderNumber() + "\n" +
                    " Customer Name: " + booking.getCustomerName() + "\n" +
                    " Contact Number: " + booking.getPhoneNumber() + "\n" +
                    " Email: " + booking.getCustomerEmail() + "\n" +
                    " Pickup Address: " + booking.getCustomerAddress() + "\n" +
                    " Destination: " + booking.getDestination() + "\n" +
                    " Scheduled Date: " + booking.getScheduledDate() + "\n" +
                    " Scheduled Time: " + booking.getScheduledTime() + "\n\n" +

                    "**Vehicle & Driver Details**\n" +
                    " Car: " + carLicensePlate + "\n" +
                    " Driver: " + driverName + "\n" +
                    " Fare Type: " + booking.getFareType() + "\n\n" +

                    "**Fare Breakdown**\n" +
                    " Base Fare: AED " + String.format("%.2f", booking.getBaseFare()) + "\n" +
                    " Distance: " + booking.getDistance() + " km\n" +
                    " Tax (" + booking.getTaxRate() + "%): AED " + String.format("%.2f", (booking.getBaseFare() * booking.getTaxRate() / 100)) + "\n" +
                    "  Discount (" + booking.getDiscountRate() + "%): AED -" + String.format("%.2f", (booking.getBaseFare() * booking.getDiscountRate() / 100)) + "\n" +
                    "  **Total Amount: AED " + String.format("%.2f", booking.getTotalAmount()) + "**\n\n" +

                    " **For Assistance:** If you have any queries, please contact our support team.\n\n" +
                    "We appreciate your business!\n\n" +
                    "Best regards,\nMegaCityCab Team";

            message.setText(emailBody);

            //  Send Email
            Transport.send(message);
            System.out.println(" Email sent successfully to " + booking.getCustomerEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println(" Failed to send email.");
        }
    }
}
