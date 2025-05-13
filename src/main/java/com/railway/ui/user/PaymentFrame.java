package com.railway.ui.user;

import com.railway.model.Booking;
import com.railway.model.Payment;
import com.railway.model.Train;
import com.railway.model.User;
import com.railway.util.PaymentGateway;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.railway.model.Seat;
import com.railway.dao.*;

public class PaymentFrame extends JFrame {
    private User currentUser;
    private Train selectedTrain;
    private List<Seat> selectedSeats;
    private double totalFare;
    
    private JComboBox<String> paymentMethodCombo;
    private JTextField cardNumberField;
    private JTextField cardHolderField;
    private JTextField expiryField;
    private JTextField cvvField;
    private JButton payButton;

    public PaymentFrame(User user, Train train, List<Seat> seats, double fare) {
        this.currentUser = user;
        this.selectedTrain = train;
        this.selectedSeats = seats;
        this.totalFare = fare;
        
        setTitle("Payment - " + train.getTrainName());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        String[] paymentMethods = {"Credit Card", "Debit Card", "Net Banking", "UPI"};
        paymentMethodCombo = new JComboBox<>(paymentMethods);
        
        cardNumberField = new JTextField(16);
        cardHolderField = new JTextField(20);
        expiryField = new JTextField(5);
        cvvField = new JTextField(3);
        
        payButton = new JButton("Pay Now ₹" + String.format("%.2f", totalFare));
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPayment();
            }
        });
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Booking summary
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(createSummaryPanel(), gbc);
        
        // Payment method
        gbc.gridy++;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(paymentMethodCombo, gbc);
        
        // Card details
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cardNumberField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Card Holder Name:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cardHolderField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Expiry (MM/YY):"), gbc);
        gbc.gridx = 1;
        mainPanel.add(expiryField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cvvField, gbc);
        
        // Pay button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        mainPanel.add(payButton, gbc);

        add(mainPanel);
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Booking Summary"));
        
        panel.add(new JLabel("Train:"));
        panel.add(new JLabel(selectedTrain.getTrainName() + " (" + selectedTrain.getTrainNumber() + ")"));
        
        panel.add(new JLabel("Route:"));
        panel.add(new JLabel(selectedTrain.getSourceStation() + " to " + selectedTrain.getDestinationStation()));
        
        panel.add(new JLabel("Departure:"));
        panel.add(new JLabel(selectedTrain.getDepartureTime().toString()));
        
        panel.add(new JLabel("Seats:"));
        StringBuilder seats = new StringBuilder();
        for (Seat seat : selectedSeats) {
            seats.append(seat.getSeatNumber()).append(", ");
        }
        panel.add(new JLabel(seats.substring(0, seats.length() - 2)));
        
        panel.add(new JLabel("Total Fare:"));
        panel.add(new JLabel("₹" + String.format("%.2f", totalFare)));
        
        return panel;
    }

    private void processPayment() {
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
        String cardNumber = cardNumberField.getText().trim();
        String cardHolder = cardHolderField.getText().trim();
        String expiry = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();

        // Validate inputs
        if (cardNumber.isEmpty() || cardHolder.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all payment details", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Process payment through gateway
            Payment payment = PaymentGateway.processPayment(
                totalFare, paymentMethod, cardNumber, cardHolder, expiry, cvv);

            if ("success".equals(payment.getStatus())) {
                // Create booking
                Booking booking = new Booking();
                booking.setUserId(currentUser.getUserId());
                booking.setTrainId(selectedTrain.getTrainId());
                booking.setTotalPassengers(selectedSeats.size());
                booking.setTotalFare(totalFare);
                
                // Save booking and payment (in a real app, this would be in a transaction)
                BookingDAO bookingDAO = new BookingDAO();
                if (bookingDAO.createBooking(booking)) {
                    PaymentDAO paymentDAO = new PaymentDAO();
                    payment.setBookingId(booking.getBookingId());
                    paymentDAO.createPayment(payment);
                    
                    // Update seat status
                    SeatDAO seatDAO = new SeatDAO();
                    for (Seat seat : selectedSeats) {
                        seatDAO.updateSeatStatus(seat.getSeatId(), true);
                    }
                    
                    // Show success message
                    JOptionPane.showMessageDialog(this, 
                        "Payment successful! Booking ID: " + booking.getBookingId(), 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to create booking", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Payment failed: " + payment.getStatus(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Payment error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}