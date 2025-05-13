package com.railway.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class Booking {
    private int bookingId;
    private int userId;
    private int trainId;
    private Timestamp bookingDate;
    private Date journeyDate;
    private int totalPassengers;
    private double totalFare;
    private String status;
    private List<Seat> seats;
    private Payment payment;

    // Constructors, getters, and setters
    public Booking() {}

    public Booking(int userId, int trainId, Date journeyDate, int totalPassengers, double totalFare) {
        this.userId = userId;
        this.trainId = trainId;
        this.journeyDate = journeyDate;
        this.totalPassengers = totalPassengers;
        this.totalFare = totalFare;
        this.status = "confirmed";
    }

    // Getters and setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getTrainId() { return trainId; }
    public void setTrainId(int trainId) { this.trainId = trainId; }
    public Timestamp getBookingDate() { return bookingDate; }
    public void setBookingDate(Timestamp bookingDate) { this.bookingDate = bookingDate; }
    public Date getJourneyDate() { return journeyDate; }
    public void setJourneyDate(Date journeyDate) { this.journeyDate = journeyDate; }
    public int getTotalPassengers() { return totalPassengers; }
    public void setTotalPassengers(int totalPassengers) { this.totalPassengers = totalPassengers; }
    public double getTotalFare() { return totalFare; }
    public void setTotalFare(double totalFare) { this.totalFare = totalFare; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId + ", Train ID: " + trainId + ", Date: " + journeyDate + 
               ", Passengers: " + totalPassengers + ", Fare: " + totalFare;
    }
}