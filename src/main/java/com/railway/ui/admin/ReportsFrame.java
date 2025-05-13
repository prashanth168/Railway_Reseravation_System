package com.railway.ui.admin;

import com.railway.service.ReportService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import com.railway.model.Train;

public class ReportsFrame extends JFrame {
    private JSpinner fromDateSpinner;
    private JSpinner toDateSpinner;
    private JButton generateReportButton;
    private JTextArea reportTextArea;

    public ReportsFrame() {
        setTitle("Generate Reports");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        fromDateSpinner = new JSpinner(new SpinnerDateModel());
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "dd/MM/yyyy"));
        
        toDateSpinner = new JSpinner(new SpinnerDateModel());
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "dd/MM/yyyy"));
        
        generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        
        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Date selection panel
        JPanel datePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        datePanel.setBorder(BorderFactory.createTitledBorder("Report Period"));
        datePanel.add(new JLabel("From Date:"));
        datePanel.add(fromDateSpinner);
        datePanel.add(new JLabel("To Date:"));
        datePanel.add(toDateSpinner);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(generateReportButton);

        // Report area
        JScrollPane reportScrollPane = new JScrollPane(reportTextArea);
        reportScrollPane.setBorder(BorderFactory.createTitledBorder("Report Output"));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(datePanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(reportScrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void generateReport() {
        Date fromDate = new Date(((java.util.Date) fromDateSpinner.getValue()).getTime());
        Date toDate = new Date(((java.util.Date) toDateSpinner.getValue()).getTime());
        
        if (fromDate.after(toDate)) {
            JOptionPane.showMessageDialog(this, 
                "From date must be before To date", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ReportService reportService = new ReportService();
        StringBuilder report = new StringBuilder();
        
        // Revenue report
        double revenue = reportService.generateRevenueReport(fromDate, toDate);
        report.append("=== REVENUE REPORT ===\n");
        report.append(String.format("Period: %s to %s\n", fromDate, toDate));
        report.append(String.format("Total Revenue: ₹%.2f\n\n", revenue));
        
        // Booking count
        int bookings = reportService.generateBookingCountReport(fromDate, toDate);
        report.append("=== BOOKINGS SUMMARY ===\n");
        report.append(String.format("Total Bookings: %d\n", bookings));
        
        // Cancellation rate
        double cancellationRate = reportService.generateCancellationRateReport(fromDate, toDate);
        report.append(String.format("Cancellation Rate: %.1f%%\n\n", cancellationRate));
        
        // Popular trains
        report.append("=== TOP 5 POPULAR TRAINS ===\n");
        List<Train> popularTrains = reportService.generatePopularTrainsReport(5);
        for (Train train : popularTrains) {
            report.append(String.format("%s (%s) - %s to %s\n", 
                train.getTrainName(), train.getTrainNumber(),
                train.getSourceStation(), train.getDestinationStation()));
        }
        
        reportTextArea.setText(report.toString());
    }
}