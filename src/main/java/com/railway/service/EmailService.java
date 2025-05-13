package com.railway.service;

import com.railway.config.EmailConfig;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
    public static boolean sendEmail(String to, String subject, String body) {
        Properties props = EmailConfig.getEmailProperties();
        String username = EmailConfig.getEmailUsername();
        String password = EmailConfig.getEmailPassword();
        String from = EmailConfig.getEmailFrom();

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendBookingConfirmation(String to, String passengerName, String bookingDetails) {
        String subject = "Railway Booking Confirmation";
        String body = createBookingEmailBody(passengerName, bookingDetails);
        return sendEmail(to, subject, body);
    }

    public static boolean sendCancellationConfirmation(String to, String passengerName, 
                                                     String bookingDetails, String refundDetails) {
        String subject = "Railway Booking Cancellation";
        String body = createCancellationEmailBody(passengerName, bookingDetails, refundDetails);
        return sendEmail(to, subject, body);
    }

    private static String createBookingEmailBody(String passengerName, String bookingDetails) {
        return String.format(
            "Dear %s,\n\n" +
            "Thank you for booking with us. Here are your booking details:\n\n" +
            "%s\n\n" +
            "Have a pleasant journey!\n\n" +
            "Regards,\nRailway Reservation System",
            passengerName, bookingDetails
        );
    }

    private static String createCancellationEmailBody(String passengerName, 
                                                    String bookingDetails, String refundDetails) {
        return String.format(
            "Dear %s,\n\n" +
            "Your booking has been cancelled successfully:\n\n" +
            "%s\n\n" +
            "Refund Details:\n%s\n\n" +
            "We hope to serve you again soon.\n\n" +
            "Regards,\nRailway Reservation System",
            passengerName, bookingDetails, refundDetails
        );
    }
}