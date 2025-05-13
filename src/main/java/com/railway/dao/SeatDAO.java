package com.railway.dao;

import com.railway.db.DatabaseConnection;
import com.railway.model.Seat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {
    private Connection connection;

    public SeatDAO() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Error establishing database connection: " + e.getMessage());
            throw e;
        }
    }

    // Get all available seats for a specific train
    public List<Seat> getAvailableSeats(int trainId) {
        List<Seat> seats = new ArrayList<>();
        String query = "SELECT s.seat_id, s.train_id, s.seat_number, s.class_type, s.is_booked, " +
                      "bs.passenger_name, bs.passenger_age, bs.passenger_gender " +
                      "FROM seats s LEFT JOIN booking_seats bs ON s.seat_id = bs.seat_id " +
                      "WHERE s.train_id = ? AND s.is_booked = false";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Seat seat = extractSeatFromResultSet(rs);
                seats.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    // Update seat status (booked or available)
    public boolean updateSeatStatus(int seatId, boolean isBooked) {
        String query = "UPDATE seats SET is_booked = ? WHERE seat_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBoolean(1, isBooked);
            stmt.setInt(2, seatId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get seat by ID
    public Seat getSeatById(int seatId) {
        String query = "SELECT s.seat_id, s.train_id, s.seat_number, s.class_type, s.is_booked, " +
                       "bs.passenger_name, bs.passenger_age, bs.passenger_gender " +
                       "FROM seats s LEFT JOIN booking_seats bs ON s.seat_id = bs.seat_id " +
                       "WHERE s.seat_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, seatId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractSeatFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all seats for a train (both available and booked)
    public List<Seat> getAllSeatsForTrain(int trainId) {
        List<Seat> seats = new ArrayList<>();
        String query = "SELECT s.seat_id, s.train_id, s.seat_number, s.class_type, s.is_booked, " +
                       "bs.passenger_name, bs.passenger_age, bs.passenger_gender " +
                       "FROM seats s LEFT JOIN booking_seats bs ON s.seat_id = bs.seat_id " +
                       "WHERE s.train_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Seat seat = extractSeatFromResultSet(rs);
                seats.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    // Get seats for a specific booking
    public List<Seat> getSeatsForBooking(int bookingId) {
        List<Seat> seats = new ArrayList<>();
        String query = "SELECT s.seat_id, s.train_id, s.seat_number, s.class_type, s.is_booked, " +
                       "bs.passenger_name, bs.passenger_age, bs.passenger_gender " +
                       "FROM seats s JOIN booking_seats bs ON s.seat_id = bs.seat_id " +
                       "WHERE bs.booking_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Seat seat = extractSeatFromResultSet(rs);
                seats.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    // Helper method to extract Seat object from ResultSet
    private Seat extractSeatFromResultSet(ResultSet rs) throws SQLException {
        Seat seat = new Seat();
        seat.setSeatId(rs.getInt("seat_id"));
        seat.setTrainId(rs.getInt("train_id"));
        seat.setSeatNumber(rs.getString("seat_number"));
        seat.setClassType(rs.getString("class_type"));
        seat.setBooked(rs.getBoolean("is_booked"));
        
        // Passenger details (may be null if seat is not booked)
        if (rs.getString("passenger_name") != null) {
            seat.setPassengerName(rs.getString("passenger_name"));
            seat.setPassengerAge(rs.getInt("passenger_age"));
            seat.setPassengerGender(rs.getString("passenger_gender"));
        }
        
        return seat;
    }

    // Assign passenger to seat (creates booking_seats record)
    public boolean assignPassengerToSeat(int bookingId, int seatId, String passengerName, 
                                       int passengerAge, String passengerGender) {
        String query = "INSERT INTO booking_seats (booking_id, seat_id, passenger_name, passenger_age, passenger_gender) " +
                       "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            stmt.setInt(2, seatId);
            stmt.setString(3, passengerName);
            stmt.setInt(4, passengerAge);
            stmt.setString(5, passengerGender);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get available seat count for a train
    public int getAvailableSeatCount(int trainId) {
        String query = "SELECT COUNT(*) FROM seats WHERE train_id = ? AND is_booked = false";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}