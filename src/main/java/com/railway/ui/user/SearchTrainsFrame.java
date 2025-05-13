package com.railway.ui.user;

import com.railway.dao.TrainDAO;
import com.railway.model.Train;
import com.railway.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SearchTrainsFrame extends JFrame {
    private User currentUser;
    private JTextField sourceField;
    private JTextField destinationField;
    private JButton searchButton;
    private JTable trainsTable;
    private JButton bookButton;

    public SearchTrainsFrame(User user) {
        this.currentUser = user;
        setTitle("Search Trains");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        sourceField = new JTextField(20);
        destinationField = new JTextField(20);
        
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTrains();
            }
        });

        // Table model
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Train No", "Train Name", "Source", "Destination", "Departure", "Arrival", "Fare"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        trainsTable = new JTable(tableModel);
        trainsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        bookButton = new JButton("Book Selected Train");
        bookButton.setEnabled(false);
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookSelectedTrain();
            }
        });

        // Enable book button when a train is selected
        trainsTable.getSelectionModel().addListSelectionListener(e -> {
            bookButton.setEnabled(trainsTable.getSelectedRow() != -1);
        });
    }

    private void layoutComponents() {
        JPanel searchPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Criteria"));
        searchPanel.add(new JLabel("Source Station:"));
        searchPanel.add(sourceField);
        searchPanel.add(new JLabel("Destination Station:"));
        searchPanel.add(destinationField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(searchButton);
        buttonPanel.add(bookButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane tableScrollPane = new JScrollPane(trainsTable);

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void searchTrains() {
        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();

        if (source.isEmpty() || destination.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both source and destination", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TrainDAO trainDAO = new TrainDAO();
        List<Train> trains = trainDAO.searchTrains(source, destination);

        DefaultTableModel model = (DefaultTableModel) trainsTable.getModel();
        model.setRowCount(0); // Clear existing rows

        if (trains.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No trains found for the given route", 
                                        "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Train train : trains) {
            model.addRow(new Object[]{
                train.getTrainNumber(),
                train.getTrainName(),
                train.getSourceStation(),
                train.getDestinationStation(),
                train.getDepartureTime(),
                train.getArrivalTime(),
                "₹" + String.format("%.2f", train.calculateFare())
            });
        }
    }

   private void bookSelectedTrain() {
        int selectedRow = trainsTable.getSelectedRow();
        if (selectedRow == -1) return;

        String trainNumber = (String) trainsTable.getValueAt(selectedRow, 0);
        String trainName = (String) trainsTable.getValueAt(selectedRow, 1);
        
        // Now using currentUser in the booking process
        JOptionPane.showMessageDialog(this, 
            "Booking train: " + trainName + " (" + trainNumber + ")\n" +
            "For user: " + currentUser.getUsername(), 
            "Booking", JOptionPane.INFORMATION_MESSAGE);
    }
}