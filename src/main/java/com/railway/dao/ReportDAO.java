package com.railway.dao;

import com.railway.db.DatabaseConnection;
import com.railway.model.Booking;
import com.railway.model.Train;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    // Get revenue report by date range
    public double getRevenueByDateRange(Date startDate, Date endDate) {
        String sql = "SELECT SUM(total_fare) AS total_revenue FROM bookings " +
                     "WHERE booking_date BETWEEN ? AND ? AND status = 'confirmed'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            System.err.println("Error getting revenue by date range: " + e.getMessage());
        }
        
        return 0.0;
    }

    // Get bookings count by date range
    public int getBookingsCountByDateRange(Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) AS booking_count FROM bookings " +
                     "WHERE booking_date BETWEEN ? AND ? AND status = 'confirmed'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("booking_count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting bookings count by date range: " + e.getMessage());
        }
        
        return 0;
    }

    // Get most popular trains
    public List<Train> getMostPopularTrains(int limit) {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT t.*, COUNT(b.booking_id) AS booking_count " +
                     "FROM trains t JOIN bookings b ON t.train_id = b.train_id " +
                     "WHERE b.status = 'confirmed' " +
                     "GROUP BY t.train_id ORDER BY booking_count DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Train train = new Train();
                train.setTrainId(rs.getInt("train_id"));
                train.setTrainNumber(rs.getString("train_number"));
                train.setTrainName(rs.getString("train_name"));
                train.setSourceStation(rs.getString("source_station"));
                train.setDestinationStation(rs.getString("destination_station"));
                train.setDepartureTime(rs.getTime("departure_time"));
                train.setArrivalTime(rs.getTime("arrival_time"));
                train.setTotalSeats(rs.getInt("total_seats"));
                train.setAvailableSeats(rs.getInt("available_seats"));
                train.setFarePerKm(rs.getDouble("fare_per_km"));
                train.setDistance(rs.getDouble("distance"));
                train.setRunningDays(rs.getString("running_days"));
                train.setActive(rs.getBoolean("is_active"));
                trains.add(train);
            }
        } catch (SQLException e) {
            System.err.println("Error getting most popular trains: " + e.getMessage());
        }
        
        return trains;
    }

    // Get cancellation rate
    public double getCancellationRate(Date startDate, Date endDate) {
        String sql = "SELECT " +
                     "(SELECT COUNT(*) FROM bookings WHERE booking_date BETWEEN ? AND ? AND status = 'cancelled') AS cancelled, " +
                     "(SELECT COUNT(*) FROM bookings WHERE booking_date BETWEEN ? AND ?) AS total";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int cancelled = rs.getInt("cancelled");
                int total = rs.getInt("total");
                return total > 0 ? (double) cancelled / total * 100 : 0.0;
            }
        } catch (SQLException e) {
            System.err.println("Error getting cancellation rate: " + e.getMessage());
        }
        
        return 0.0;
    }

    // Get bookings by status
    public List<Booking> getBookingsByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE status = ? ORDER BY booking_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setTrainId(rs.getInt("train_id"));
                booking.setBookingDate(rs.getTimestamp("booking_date"));
                booking.setJourneyDate(rs.getDate("journey_date"));
                booking.setTotalPassengers(rs.getInt("total_passengers"));
                booking.setTotalFare(rs.getDouble("total_fare"));
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error getting bookings by status: " + e.getMessage());
        }
        
        return bookings;
    }
}