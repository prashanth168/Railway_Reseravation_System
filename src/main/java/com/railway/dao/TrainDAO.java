package com.railway.dao;

import com.railway.db.DatabaseConnection;
import com.railway.model.Train;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO {
    // Add a new train
    public boolean addTrain(Train train) {
        String sql = "INSERT INTO trains (train_number, train_name, source_station, destination_station, " +
                     "departure_time, arrival_time, total_seats, available_seats, fare_per_km, distance, running_days) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, train.getTrainNumber());
            stmt.setString(2, train.getTrainName());
            stmt.setString(3, train.getSourceStation());
            stmt.setString(4, train.getDestinationStation());
            stmt.setTime(5, train.getDepartureTime());
            stmt.setTime(6, train.getArrivalTime());
            stmt.setInt(7, train.getTotalSeats());
            stmt.setInt(8, train.getAvailableSeats());
            stmt.setDouble(9, train.getFarePerKm());
            stmt.setDouble(10, train.getDistance());
            stmt.setString(11, train.getRunningDays());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    train.setTrainId(generatedKeys.getInt(1));
                }
            }
            
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding train: " + e.getMessage());
            return false;
        }
    }

    // Get train by ID
    public Train getTrainById(int trainId) {
        String sql = "SELECT * FROM trains WHERE train_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractTrainFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting train by ID: " + e.getMessage());
        }
        
        return null;
    }

    // Get all trains
    public List<Train> getAllTrains() {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT * FROM trains WHERE is_active = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                trains.add(extractTrainFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all trains: " + e.getMessage());
        }
        
        return trains;
    }

    // Search trains by source and destination
    public List<Train> searchTrains(String source, String destination) {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT * FROM trains WHERE source_station LIKE ? AND destination_station LIKE ? AND is_active = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + source + "%");
            stmt.setString(2, "%" + destination + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                trains.add(extractTrainFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching trains: " + e.getMessage());
        }
        
        return trains;
    }

    // Update train
    public boolean updateTrain(Train train) {
        String sql = "UPDATE trains SET train_number = ?, train_name = ?, source_station = ?, " +
                     "destination_station = ?, departure_time = ?, arrival_time = ?, total_seats = ?, " +
                     "available_seats = ?, fare_per_km = ?, distance = ?, running_days = ?, is_active = ? " +
                     "WHERE train_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, train.getTrainNumber());
            stmt.setString(2, train.getTrainName());
            stmt.setString(3, train.getSourceStation());
            stmt.setString(4, train.getDestinationStation());
            stmt.setTime(5, train.getDepartureTime());
            stmt.setTime(6, train.getArrivalTime());
            stmt.setInt(7, train.getTotalSeats());
            stmt.setInt(8, train.getAvailableSeats());
            stmt.setDouble(9, train.getFarePerKm());
            stmt.setDouble(10, train.getDistance());
            stmt.setString(11, train.getRunningDays());
            stmt.setBoolean(12, train.isActive());
            stmt.setInt(13, train.getTrainId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating train: " + e.getMessage());
            return false;
        }
    }

    // Delete train (soft delete)
    public boolean deleteTrain(int trainId) {
        String sql = "UPDATE trains SET is_active = FALSE WHERE train_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, trainId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting train: " + e.getMessage());
            return false;
        }
    }

    // Helper method to extract train from result set
    private Train extractTrainFromResultSet(ResultSet rs) throws SQLException {
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
        return train;
    }

    // Update available seats
    public boolean updateAvailableSeats(int trainId, int seatsBooked) {
        String sql = "UPDATE trains SET available_seats = available_seats - ? WHERE train_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, seatsBooked);
            stmt.setInt(2, trainId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating available seats: " + e.getMessage());
            return false;
        }
    }
}