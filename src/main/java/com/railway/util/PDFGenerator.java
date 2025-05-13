package com.railway.util;

import com.railway.model.Booking;
import com.railway.model.Payment;
import com.railway.model.Seat;
import com.railway.model.Train;
import com.railway.model.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFGenerator {
    public static boolean generateBookingReceipt(Booking booking, User user, Train train, 
                                              List<Seat> seats, Payment payment, String filePath) {
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
            Paragraph title = new Paragraph("RAILWAY BOOKING RECEIPT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Add booking details
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            
            // User details
            document.add(new Paragraph("Passenger Details:", headerFont));
            document.add(new Paragraph("Name: " + user.getFirstName() + " " + user.getLastName(), contentFont));
            document.add(new Paragraph("Email: " + user.getEmail(), contentFont));
            document.add(new Paragraph("Phone: " + user.getPhone(), contentFont));
            document.add(new Paragraph(" "));
            
            // Booking details
            document.add(new Paragraph("Booking Details:", headerFont));
            document.add(new Paragraph("Booking ID: " + booking.getBookingId(), contentFont));
            document.add(new Paragraph("Booking Date: " + formatDate(booking.getBookingDate()), contentFont));
            document.add(new Paragraph("Journey Date: " + formatDate(booking.getJourneyDate()), contentFont));
            document.add(new Paragraph(" "));
            
            // Train details
            document.add(new Paragraph("Train Details:", headerFont));
            document.add(new Paragraph("Train: " + train.getTrainName() + " (" + train.getTrainNumber() + ")", contentFont));
            document.add(new Paragraph("Route: " + train.getSourceStation() + " to " + train.getDestinationStation(), contentFont));
            document.add(new Paragraph("Departure: " + train.getDepartureTime() + " | Arrival: " + train.getArrivalTime(), contentFont));
            document.add(new Paragraph(" "));
            
            // Seats table
            document.add(new Paragraph("Seat Details:", headerFont));
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Add table headers
            addTableHeader(table, "Seat Number");
            addTableHeader(table, "Class");
            addTableHeader(table, "Passenger Name");
            addTableHeader(table, "Age/Gender");
            
            // Add seat data
            for (Seat seat : seats) {
                table.addCell(seat.getSeatNumber());
                table.addCell(seat.getClassType());
                table.addCell(seat.getPassengerName());
                table.addCell(seat.getPassengerAge() + "/" + seat.getPassengerGender());
            }
            
            document.add(table);
            document.add(new Paragraph(" "));
            
            // Payment details
            document.add(new Paragraph("Payment Details:", headerFont));
            document.add(new Paragraph("Amount: ₹" + String.format("%.2f", payment.getAmount()), contentFont));
            document.add(new Paragraph("Payment Method: " + payment.getPaymentMethod(), contentFont));
            document.add(new Paragraph("Transaction ID: " + payment.getTransactionId(), contentFont));
            document.add(new Paragraph("Status: " + payment.getStatus(), contentFont));
            document.add(new Paragraph(" "));
            
            // Footer
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);
            Paragraph footer = new Paragraph("Thank you for choosing our service. Have a safe journey!", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);
            
            document.close();
            return true;
        } catch (DocumentException | IOException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            return false;
        }
    }
    
    private static void addTableHeader(PdfPTable table, String header) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setBorderWidth(1);
        cell.setPhrase(new Phrase(header));
        table.addCell(cell);
    }
    
    private static String formatDate(java.util.Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return sdf.format(date);
    }
    
    private static String formatDate(java.sql.Date date) {
        if (date == null) return "";
        return formatDate(new Date(date.getTime()));
    }
    
    private static String formatDate(java.sql.Timestamp timestamp) {
        if (timestamp == null) return "";
        return formatDate(new Date(timestamp.getTime()));
    }
}