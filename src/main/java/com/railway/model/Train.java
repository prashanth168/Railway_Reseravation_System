package com.railway.model;

import java.sql.Time;

public class Train {
    private int trainId;
    private String trainNumber;
    private String trainName;
    private String sourceStation;
    private String destinationStation;
    private Time departureTime;
    private Time arrivalTime;
    private int totalSeats;
    private int availableSeats;
    private double farePerKm;
    private double distance;
    private String runningDays;
    private boolean isActive;

    // Constructors, getters, and setters
    public Train() {}

    public Train(String trainNumber, String trainName, String sourceStation, String destinationStation, 
                 Time departureTime, Time arrivalTime, int totalSeats, double farePerKm, 
                 double distance, String runningDays) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.farePerKm = farePerKm;
        this.distance = distance;
        this.runningDays = runningDays;
        this.isActive = true;
    }

    // Getters and setters
    public int getTrainId() { return trainId; }
    public void setTrainId(int trainId) { this.trainId = trainId; }
    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public String getSourceStation() { return sourceStation; }
    public void setSourceStation(String sourceStation) { this.sourceStation = sourceStation; }
    public String getDestinationStation() { return destinationStation; }
    public void setDestinationStation(String destinationStation) { this.destinationStation = destinationStation; }
    public Time getDepartureTime() { return departureTime; }
    public void setDepartureTime(Time departureTime) { this.departureTime = departureTime; }
    public Time getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(Time arrivalTime) { this.arrivalTime = arrivalTime; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public double getFarePerKm() { return farePerKm; }
    public void setFarePerKm(double farePerKm) { this.farePerKm = farePerKm; }
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public String getRunningDays() { return runningDays; }
    public void setRunningDays(String runningDays) { this.runningDays = runningDays; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public double calculateFare() {
        return distance * farePerKm;
    }

    @Override
    public String toString() {
        return trainName + " (" + trainNumber + ") - " + sourceStation + " to " + destinationStation;
    }
}