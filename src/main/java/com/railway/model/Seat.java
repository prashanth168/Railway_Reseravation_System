package com.railway.model;

public class Seat {
    private int seatId;
    private int trainId;
    private String seatNumber;
    private String classType;
    private boolean isBooked;
    private String passengerName;
    private int passengerAge;
    private String passengerGender;

    public Seat() {}

    public Seat(int trainId, String seatNumber, String classType) {
        this.trainId = trainId;
        this.seatNumber = seatNumber;
        this.classType = classType;
        this.isBooked = false;
    }

    // Getters and Setters
    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    // Passenger information methods
    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getPassengerAge() {
        return passengerAge;
    }

    public void setPassengerAge(int passengerAge) {
        this.passengerAge = passengerAge;
    }

    public String getPassengerGender() {
        return passengerGender;
    }

    public void setPassengerGender(String passengerGender) {
        this.passengerGender = passengerGender;
    }

    @Override
    public String toString() {
        return seatNumber + " (" + classType + ") - " + (isBooked ? "Booked" : "Available");
    }
}