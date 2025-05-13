package com.railway.ui.admin;

import com.railway.model.User;
import com.railway.ui.auth.LoginFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {
    private User currentUser;

    public AdminDashboard(User user) {
        this.currentUser = user;
        setTitle("Railway Reservation System - Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        // Menu items will be initialized here
    }

    private void layoutComponents() {
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // Trains menu
        JMenu trainsMenu = new JMenu("Trains");
        JMenuItem manageTrainsItem = new JMenuItem("Manage Trains");
        manageTrainsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openManageTrains();
            }
        });
        trainsMenu.add(manageTrainsItem);

        // Bookings menu
        JMenu bookingsMenu = new JMenu("Bookings");
        JMenuItem viewBookingsItem = new JMenuItem("View Bookings");
        viewBookingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewBookingsFrame().setVisible(true);
            }
        });
        bookingsMenu.add(viewBookingsItem);

        // Reports menu
        JMenu reportsMenu = new JMenu("Reports");
        JMenuItem generateReportsItem = new JMenuItem("Generate Reports");
        generateReportsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReportsFrame().setVisible(true);
            }
        });
        reportsMenu.add(generateReportsItem);

        // User menu
        JMenu userMenu = new JMenu("User");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        userMenu.add(logoutItem);

        // Add menus to menu bar
        menuBar.add(trainsMenu);
        menuBar.add(bookingsMenu);
        menuBar.add(reportsMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(userMenu);

        setJMenuBar(menuBar);

        // Welcome panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName() + 
                                       " (Admin)", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        mainPanel.add(new JLabel("Admin Dashboard - Select an option from the menu", JLabel.CENTER), BorderLayout.CENTER);

        add(mainPanel);
    }

    private void openManageTrains() {
        new ManageTrainsFrame().setVisible(true);
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}