package com.railway.ui.user;

import com.railway.dao.SeatDAO;
import com.railway.model.Seat;
import com.railway.model.Train;
import com.railway.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionFrame extends JFrame {
    private User currentUser;
    private Train selectedTrain;
    private JList<Seat> seatList;
    private JSpinner passengerCountSpinner;
    private JButton confirmButton;
    private List<Seat> selectedSeats = new ArrayList<>();

    public SeatSelectionFrame(User user, Train train) throws SQLException{
        this.currentUser = user;
        this.selectedTrain = train;
        setTitle("Seat Selection - " + train.getTrainName());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        loadAvailableSeats();
    }

    private void initComponents() throws SQLException {
        SeatDAO seatDAO = new SeatDAO();
        List<Seat> availableSeats = seatDAO.getAvailableSeats(selectedTrain.getTrainId());
        
        seatList = new JList<>(availableSeats.toArray(new Seat[0]));
        seatList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        seatList.setCellRenderer(new SeatListRenderer());
        
        passengerCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        
        confirmButton = new JButton("Confirm Selection");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmSelection();
            }
        });
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Train info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Train Information"));
        infoPanel.add(new JLabel("Train Number:"));
        infoPanel.add(new JLabel(selectedTrain.getTrainNumber()));
        infoPanel.add(new JLabel("Train Name:"));
        infoPanel.add(new JLabel(selectedTrain.getTrainName()));
        infoPanel.add(new JLabel("Route:"));
        infoPanel.add(new JLabel(selectedTrain.getSourceStation() + " to " + selectedTrain.getDestinationStation()));
        infoPanel.add(new JLabel("Available Seats:"));
        infoPanel.add(new JLabel(String.valueOf(selectedTrain.getAvailableSeats())));

        // Seat selection panel
        JPanel seatPanel = new JPanel(new BorderLayout());
        seatPanel.setBorder(BorderFactory.createTitledBorder("Available Seats"));
        seatPanel.add(new JScrollPane(seatList), BorderLayout.CENTER);

        // Passenger count panel
        JPanel passengerPanel = new JPanel();
        passengerPanel.add(new JLabel("Number of Passengers:"));
        passengerPanel.add(passengerCountSpinner);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(confirmButton);

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(seatPanel, BorderLayout.CENTER);
        mainPanel.add(passengerPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAvailableSeats() throws SQLException {
        SeatDAO seatDAO = new SeatDAO();
        List<Seat> availableSeats = seatDAO.getAvailableSeats(selectedTrain.getTrainId());
        seatList.setListData(availableSeats.toArray(new Seat[0]));
    }

    private void confirmSelection() {
        selectedSeats.clear();
        selectedSeats.addAll(seatList.getSelectedValuesList());
        
        int passengerCount = (Integer) passengerCountSpinner.getValue();
        
        if (selectedSeats.size() != passengerCount) {
            JOptionPane.showMessageDialog(this, 
                "Please select exactly " + passengerCount + " seat(s)", 
                "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Open passenger details frame
        PassengerDetailsFrame detailsFrame = new PassengerDetailsFrame(
            currentUser, selectedTrain, selectedSeats);
        detailsFrame.setVisible(true);
        dispose();
    }

    // Custom renderer for seat list
    private class SeatListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Seat) {
                Seat seat = (Seat) value;
                setText(seat.getSeatNumber() + " (" + seat.getClassType() + ")");
            }
            return this;
        }
    }
}