package com.railway.dao;

import com.railway.db.DatabaseConnection;
import com.railway.model.Booking;
import com.railway.model.Seat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    // Create a new booking
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, train_id, journey_date, total_passengers, total_fare, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getTrainId());
            stmt.setDate(3, booking.getJourneyDate());
            stmt.setInt(4, booking.getTotalPassengers());
            stmt.setDouble(5, booking.getTotalFare());
            stmt.setString(6, booking.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setBookingId(generatedKeys.getInt(1));
                }
            }
            
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            return false;
        }
    }

    // Get booking by ID
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractBookingFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting booking by ID: " + e.getMessage());
        }
        
        return null;
    }

    // Get bookings by user ID
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY booking_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting bookings by user ID: " + e.getMessage());
        }
        
        return bookings;
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY booking_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all bookings: " + e.getMessage());
        }
        
        return bookings;
    }

    // Cancel booking
    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET status = 'cancelled' WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
            return false;
        }
    }

    // Helper method to extract booking from result set
    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setTrainId(rs.getInt("train_id"));
        booking.setBookingDate(rs.getTimestamp("booking_date"));
        booking.setJourneyDate(rs.getDate("journey_date"));
        booking.setTotalPassengers(rs.getInt("total_passengers"));
        booking.setTotalFare(rs.getDouble("total_fare"));
        booking.setStatus(rs.getString("status"));
        return booking;
    }

    // Add seats to booking
    public boolean addSeatsToBooking(int bookingId, List<Seat> seats) {
        String sql = "INSERT INTO booking_seats (booking_id, seat_id, passenger_name, passenger_age, passenger_gender) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (Seat seat : seats) {
                stmt.setInt(1, bookingId);
                stmt.setInt(2, seat.getSeatId());
                stmt.setString(3, seat.getPassengerName());
                stmt.setInt(4, seat.getPassengerAge());
                stmt.setString(5, seat.getPassengerGender());
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding seats to booking: " + e.getMessage());
            return false;
        }
    }

    // Get seats for booking
    public List<Seat> getSeatsForBooking(int bookingId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT s.seat_id, s.train_id, s.seat_number, s.class_type, " +
                     "bs.passenger_name, bs.passenger_age, bs.passenger_gender " +
                     "FROM seats s JOIN booking_seats bs ON s.seat_id = bs.seat_id " +
                     "WHERE bs.booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatId(rs.getInt("seat_id"));
                seat.setTrainId(rs.getInt("train_id"));
                seat.setSeatNumber(rs.getString("seat_number"));
                seat.setClassType(rs.getString("class_type"));
                seat.setPassengerName(rs.getString("passenger_name"));
                seat.setPassengerAge(rs.getInt("passenger_age"));
                seat.setPassengerGender(rs.getString("passenger_gender"));
                seat.setBooked(true);
                seats.add(seat);
            }
        } catch (SQLException e) {
            System.err.println("Error getting seats for booking: " + e.getMessage());
        }
        
        return seats;
    }
}