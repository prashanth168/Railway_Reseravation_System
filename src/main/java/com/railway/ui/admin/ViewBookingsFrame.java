package com.railway.ui.admin;

import javax.swing.*;

public class ViewBookingsFrame extends JFrame {
    public ViewBookingsFrame() {
        setTitle("View Bookings");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Add your booking viewing components here
        add(new JLabel("Booking Management Panel", JLabel.CENTER));
    }
}