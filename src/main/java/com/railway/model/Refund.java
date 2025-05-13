package com.railway.model;

import java.sql.Timestamp;

public class Refund {
    private int refundId;
    private int bookingId;
    private int paymentId;
    private double amount;
    private Timestamp refundDate;
    private String status;
    private String reason;

    // Constructors, getters, and setters
    public Refund() {}

    public Refund(int bookingId, int paymentId, double amount, String status, String reason) {
        this.bookingId = bookingId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status;
        this.reason = reason;
    }

    // Getters and setters
    public int getRefundId() { return refundId; }
    public void setRefundId(int refundId) { this.refundId = refundId; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Timestamp getRefundDate() { return refundDate; }
    public void setRefundDate(Timestamp refundDate) { this.refundDate = refundDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    @Override
    public String toString() {
        return "Refund ID: " + refundId + ", Amount: " + amount + ", Status: " + status;
    }
}