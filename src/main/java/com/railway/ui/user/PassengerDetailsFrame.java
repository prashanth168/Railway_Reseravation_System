package com.railway.ui.user;

import com.railway.model.Seat;
import com.railway.model.Train;
import com.railway.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PassengerDetailsFrame extends JFrame {
    private User currentUser;
    private Train selectedTrain;
    private List<Seat> selectedSeats;
    
    private JTextField[] nameFields;
    private JSpinner[] ageSpinners;
    private JComboBox<String>[] genderCombos;
    private JButton submitButton;

    public PassengerDetailsFrame(User user, Train train, List<Seat> seats) {
        this.currentUser = user;
        this.selectedTrain = train;
        this.selectedSeats = new ArrayList<>(seats);
        
        setTitle("Passenger Details");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        int passengerCount = selectedSeats.size();
        nameFields = new JTextField[passengerCount];
        ageSpinners = new JSpinner[passengerCount];
        genderCombos = (JComboBox<String>[]) new JComboBox[passengerCount];
        
        String[] genders = {"Male", "Female", "Other"};
        
        for (int i = 0; i < passengerCount; i++) {
            nameFields[i] = new JTextField(20);
            ageSpinners[i] = new JSpinner(new SpinnerNumberModel(25, 1, 100, 1));
            genderCombos[i] = new JComboBox<>(genders);
        }
        
        submitButton = new JButton("Submit Details");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitDetails();
            }
        });
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Passenger details panel
        JPanel detailsPanel = new JPanel(new GridLayout(0, 4, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Enter Passenger Details"));
        
        // Headers
        detailsPanel.add(new JLabel("Seat Number"));
        detailsPanel.add(new JLabel("Passenger Name"));
        detailsPanel.add(new JLabel("Age"));
        detailsPanel.add(new JLabel("Gender"));
        
        // Rows for each passenger
        for (int i = 0; i < selectedSeats.size(); i++) {
            detailsPanel.add(new JLabel(selectedSeats.get(i).getSeatNumber()));
            detailsPanel.add(nameFields[i]);
            detailsPanel.add(ageSpinners[i]);
            detailsPanel.add(genderCombos[i]);
        }
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(submitButton);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void submitDetails() {
        for (int i = 0; i < nameFields.length; i++) {
            if (nameFields[i].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter name for passenger in seat " + selectedSeats.get(i).getSeatNumber(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        for (int i = 0; i < selectedSeats.size(); i++) {
            Seat seat = selectedSeats.get(i);
            seat.setPassengerName(nameFields[i].getText().trim());
            seat.setPassengerAge((Integer) ageSpinners[i].getValue());
            seat.setPassengerGender(genderCombos[i].getSelectedItem().toString().toLowerCase());
        }
        
        double totalFare = selectedTrain.calculateFare() * selectedSeats.size();
        
        new PaymentFrame(currentUser, selectedTrain, selectedSeats, totalFare).setVisible(true);
        dispose();
    }
}