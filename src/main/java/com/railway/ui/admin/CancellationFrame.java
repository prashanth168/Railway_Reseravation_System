package com.railway.ui.admin;

import com.railway.dao.BookingDAO;
import com.railway.dao.PaymentDAO;
import com.railway.model.Booking;
import com.railway.model.Payment;
import com.railway.service.PaymentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CancellationFrame extends JFrame {
    private JTable cancellationsTable;
    private JButton processRefundButton;
    private JButton refreshButton;
    private PaymentDAO paymentDAO; 

    public CancellationFrame() {
        paymentDAO = new PaymentDAO();
        setTitle("Manage Cancellations");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        loadCancellations();
    }

    private void initComponents() {
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Booking ID", "User ID", "Train", "Journey Date", "Fare", 
                        "Payment ID", "Amount", "Refund Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cancellationsTable = new JTable(tableModel);
        cancellationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        processRefundButton = new JButton("Process Refund");
        processRefundButton.setEnabled(false);
        processRefundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processRefund();
            }
        });
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCancellations();
            }
        });

        cancellationsTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = cancellationsTable.getSelectedRow() != -1;
            String status = rowSelected ? (String) cancellationsTable.getValueAt(
                cancellationsTable.getSelectedRow(), 7) : "";
            processRefundButton.setEnabled(rowSelected && !"processed".equals(status));
        });
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        JScrollPane tableScrollPane = new JScrollPane(cancellationsTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(processRefundButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadCancellations() {
        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> cancelledBookings = bookingDAO.getAllBookings();
        
        DefaultTableModel model = (DefaultTableModel) cancellationsTable.getModel();
        model.setRowCount(0);

        for (Booking booking : cancelledBookings) {
            if ("cancelled".equals(booking.getStatus())) {
                List<Payment> payments = paymentDAO.getPaymentsByBookingId(booking.getBookingId());
                
                if (!payments.isEmpty()) {
                    Payment payment = payments.get(0);
                    // Changed from payment.isRefundProcessed() to checking status directly
                    String refundStatus = "refunded".equalsIgnoreCase(payment.getStatus()) ? 
                                         "processed" : "pending";
                    
                    model.addRow(new Object[]{
                        booking.getBookingId(),
                        booking.getUserId(),
                        "Train #" + booking.getTrainId(),
                        booking.getJourneyDate(),
                        "₹" + String.format("%.2f", booking.getTotalFare()),
                        payment.getPaymentId(),
                        "₹" + String.format("%.2f", payment.getAmount()),
                        refundStatus
                    });
                }
            }
        }
    }

    private void processRefund() {
        int selectedRow = cancellationsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int bookingId = (Integer) cancellationsTable.getValueAt(selectedRow, 0);
        int paymentId = (Integer) cancellationsTable.getValueAt(selectedRow, 5);
        double amount = Double.parseDouble(
            ((String) cancellationsTable.getValueAt(selectedRow, 6)).substring(1));
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Process refund of ₹" + String.format("%.2f", amount) + " for Booking ID " + bookingId + "?",
            "Confirm Refund", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Payment refund = PaymentService.processRefund(bookingId, paymentId, amount, "Cancellation");
            
            // Changed from static call to instance method
            if (paymentDAO.updateRefundStatus(paymentId, true)) {
                JOptionPane.showMessageDialog(this,
                    "Refund processed successfully! Transaction ID: " + refund.getTransactionId(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCancellations();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to record refund in database",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}