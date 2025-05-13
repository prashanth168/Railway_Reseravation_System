package com.railway.util;

import com.railway.model.Payment;
import java.util.UUID;

public class PaymentGateway {
    // This is a mock implementation - in a real application, this would connect to an actual payment gateway
    public static Payment processPayment(double amount, String paymentMethod, String cardNumber, 
                                      String cardHolder, String expiry, String cvv) {
        // Validate payment details
        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid card number");
        }
        
        if (expiry == null || !expiry.matches("\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Invalid expiry date (MM/YY format required)");
        }
        
        if (cvv == null || cvv.length() != 3 || !cvv.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid CVV (3 digits required)");
        }
        
        // Simulate payment processing
        try {
            Thread.sleep(2000); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate a 10% chance of payment failure
        if (Math.random() < 0.1) {
            Payment failedPayment = new Payment();
            failedPayment.setAmount(amount);
            failedPayment.setPaymentMethod(paymentMethod);
            failedPayment.setTransactionId("FAILED-" + System.currentTimeMillis());
            failedPayment.setStatus("failed");
            return failedPayment;
        }
        
        // Successful payment
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId(generateTransactionId());
        payment.setStatus("success");
        return payment;
    }

    public static Payment processRefund(int bookingId, int paymentId, double amount, String reason) {
        // Simulate refund processing
        try {
            Thread.sleep(2000); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Successful refund
        Payment refund = new Payment();
        refund.setBookingId(bookingId);
        refund.setAmount(amount);
        refund.setPaymentMethod("refund");
        refund.setTransactionId("REFUND-" + generateTransactionId());
        refund.setStatus("processed");
        return refund;
    }

    private static String generateTransactionId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}