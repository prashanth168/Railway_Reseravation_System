package com.railway.ui.admin;

import com.railway.dao.TrainDAO;
import com.railway.model.Train;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageTrainsFrame extends JFrame {
    private JTable trainsTable;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    public ManageTrainsFrame() {
        setTitle("Manage Trains");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        loadTrains();
    }

    private void initComponents() {
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Train ID", "Number", "Name", "Source", "Destination", 
                        "Departure", "Arrival", "Seats", "Fare/km", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        trainsTable = new JTable(tableModel);
        trainsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        addButton = new JButton("Add Train");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTrain();
            }
        });
        
        editButton = new JButton("Edit Train");
        editButton.setEnabled(false);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editTrain();
            }
        });
        
        deleteButton = new JButton("Delete Train");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTrain();
            }
        });

        // Enable buttons when a train is selected
        trainsTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = trainsTable.getSelectedRow() != -1;
            editButton.setEnabled(rowSelected);
            deleteButton.setEnabled(rowSelected);
        });
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        JScrollPane tableScrollPane = new JScrollPane(trainsTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadTrains() {
        TrainDAO trainDAO = new TrainDAO();
        List<Train> trains = trainDAO.getAllTrains();
        
        DefaultTableModel model = (DefaultTableModel) trainsTable.getModel();
        model.setRowCount(0); // Clear existing rows

        for (Train train : trains) {
            model.addRow(new Object[]{
                train.getTrainId(),
                train.getTrainNumber(),
                train.getTrainName(),
                train.getSourceStation(),
                train.getDestinationStation(),
                train.getDepartureTime(),
                train.getArrivalTime(),
                train.getAvailableSeats() + "/" + train.getTotalSeats(),
                "₹" + String.format("%.2f", train.getFarePerKm()),
                train.isActive() ? "Active" : "Inactive"
            });
        }
    }

    private void addTrain() {
        // In a real app, you would create a form dialog
        JOptionPane.showMessageDialog(this, 
            "Add Train functionality would be implemented here", 
            "Add Train", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editTrain() {
        int selectedRow = trainsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int trainId = (Integer) trainsTable.getValueAt(selectedRow, 0);
        // In a real app, you would open an edit dialog
        JOptionPane.showMessageDialog(this, 
            "Edit Train ID: " + trainId, 
            "Edit Train", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteTrain() {
        int selectedRow = trainsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int trainId = (Integer) trainsTable.getValueAt(selectedRow, 0);
        String trainName = (String) trainsTable.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete " + trainName + "?", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            TrainDAO trainDAO = new TrainDAO();
            if (trainDAO.deleteTrain(trainId)) {
                JOptionPane.showMessageDialog(this, 
                    "Train deleted successfully", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTrains(); // Refresh the list
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to delete train", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}