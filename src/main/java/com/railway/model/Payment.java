package com.railway.model;

import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int bookingId;
    private double amount;
    private Timestamp paymentDate;
    private String paymentMethod;
    private String transactionId;
    private String status;

    // Constructors, getters, and setters
    public Payment() {}

    public Payment(int bookingId, double amount, String paymentMethod, String transactionId, String status) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status = status;
    }

    // Getters and setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Timestamp getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Timestamp paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Payment ID: " + paymentId + ", Amount: " + amount + ", Method: " + paymentMethod + 
               ", Status: " + status;
    }
}