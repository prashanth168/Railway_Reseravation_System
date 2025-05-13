package com.railway.dao;

import com.railway.db.DatabaseConnection;
import com.railway.model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    // Create a new payment
    public boolean createPayment(Payment payment) {
        String sql = "INSERT INTO payments (booking_id, amount, payment_method, transaction_id, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, payment.getBookingId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setString(4, payment.getTransactionId());
            stmt.setString(5, payment.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setPaymentId(generatedKeys.getInt(1));
                    payment.setPaymentDate(generatedKeys.getTimestamp("payment_date"));
                }
            }
            
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating payment: " + e.getMessage());
            return false;
        }
    }

    // Get payment by ID
    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment by ID: " + e.getMessage());
        }
        
        return null;
    }

    // Get payments by booking ID
    public List<Payment> getPaymentsByBookingId(int bookingId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE booking_id = ? ORDER BY payment_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting payments by booking ID: " + e.getMessage());
        }
        
        return payments;
    }

    // Update payment status
    public boolean updatePaymentStatus(int paymentId, String status) {
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, paymentId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating payment status: " + e.getMessage());
            return false;
        }
    }

    // Helper method to extract payment from result set
    private Payment extractPaymentFromResultSet(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setBookingId(rs.getInt("booking_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentDate(rs.getTimestamp("payment_date"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setTransactionId(rs.getString("transaction_id"));
        payment.setStatus(rs.getString("status"));
        return payment;
    }
    public boolean isRefundProcessed(int paymentId) {
        String sql = "SELECT status FROM payments WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return "refunded".equalsIgnoreCase(rs.getString("status"));
            }
        } catch (SQLException e) {
            System.err.println("Error checking refund status: " + e.getMessage());
        }
        return false;
    }

    // Add this new method to update refund status
    public boolean updateRefundStatus(int paymentId, boolean isRefunded) {
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isRefunded ? "refunded" : "failed");
            stmt.setInt(2, paymentId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating refund status: " + e.getMessage());
            return false;
        }
    }
}