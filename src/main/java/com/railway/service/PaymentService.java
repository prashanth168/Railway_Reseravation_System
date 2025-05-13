package com.railway.service;

import com.railway.model.Payment;
import java.util.UUID;

public class PaymentService {
    public static Payment processPayment(int bookingId, double amount, String paymentMethod) {
        // In a real application, this would integrate with a payment gateway
        // For this example, we'll simulate a successful payment
        
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId(generateTransactionId());
        payment.setStatus("success");
        
        return payment;
    }

    public static Payment processRefund(int bookingId, int paymentId, double amount, String reason) {
        // Simulate a refund process
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