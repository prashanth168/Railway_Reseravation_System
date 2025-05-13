package com.railway.ui.user;

import com.railway.dao.BookingDAO;
import com.railway.model.Booking;
import com.railway.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MyBookingsFrame extends JFrame {
    private User currentUser;
    private JTable bookingsTable;
    private JButton viewDetailsButton;
    private JButton cancelButton;

    public MyBookingsFrame(User user) {
        this.currentUser = user;
        setTitle("My Bookings");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        loadBookings();
    }

    private void initComponents() {
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Booking ID", "Train", "Journey Date", "Passengers", "Fare", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookingsTable = new JTable(tableModel);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setEnabled(false);
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewBookingDetails();
            }
        });
        
        cancelButton = new JButton("Cancel Booking");
        cancelButton.setEnabled(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelBooking();
            }
        });

        // Enable buttons when a booking is selected
        bookingsTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = bookingsTable.getSelectedRow() != -1;
            viewDetailsButton.setEnabled(rowSelected);
            cancelButton.setEnabled(rowSelected);
        });
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        JScrollPane tableScrollPane = new JScrollPane(bookingsTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadBookings() {
        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> bookings = bookingDAO.getBookingsByUserId(currentUser.getUserId());
        
        DefaultTableModel model = (DefaultTableModel) bookingsTable.getModel();
        model.setRowCount(0); // Clear existing rows

        for (Booking booking : bookings) {
            // In a real app, you would get train details from DAO
            String trainInfo = "Train #" + booking.getTrainId();
            model.addRow(new Object[]{
                booking.getBookingId(),
                trainInfo,
                booking.getJourneyDate(),
                booking.getTotalPassengers(),
                "₹" + String.format("%.2f", booking.getTotalFare()),
                booking.getStatus()
            });
        }
    }

    private void viewBookingDetails() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int bookingId = (Integer) bookingsTable.getValueAt(selectedRow, 0);
        // In a real app, show detailed view with seat numbers, etc.
        JOptionPane.showMessageDialog(this, 
            "Details for Booking ID: " + bookingId, 
            "Booking Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int bookingId = (Integer) bookingsTable.getValueAt(selectedRow, 0);
        String status = (String) bookingsTable.getValueAt(selectedRow, 5);
        
        if ("cancelled".equals(status)) {
            JOptionPane.showMessageDialog(this, 
                "This booking is already cancelled", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to cancel this booking?", 
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            BookingDAO bookingDAO = new BookingDAO();
            if (bookingDAO.cancelBooking(bookingId)) {
                JOptionPane.showMessageDialog(this, 
                    "Booking cancelled successfully", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBookings(); // Refresh the list
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to cancel booking", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}