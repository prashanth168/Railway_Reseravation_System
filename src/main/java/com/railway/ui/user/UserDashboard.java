package com.railway.ui.user;

import com.railway.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.railway.ui.auth.LoginFrame;
public class UserDashboard extends JFrame {
    private User currentUser;

    public UserDashboard(User user) {
        this.currentUser = user;
        setTitle("Railway Reservation System - User Dashboard");
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

        // Bookings menu
        JMenu bookingsMenu = new JMenu("Bookings");
        JMenuItem searchTrainsItem = new JMenuItem("Search Trains");
        searchTrainsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSearchTrains();
            }
        });
        bookingsMenu.add(searchTrainsItem);

        JMenuItem myBookingsItem = new JMenuItem("My Bookings");
        myBookingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMyBookings();
            }
        });
        bookingsMenu.add(myBookingsItem);

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
        menuBar.add(bookingsMenu);
        menuBar.add(Box.createHorizontalGlue()); // Right-align the user menu
        menuBar.add(userMenu);

        setJMenuBar(menuBar);

        // Welcome panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName(), 
                                       JLabel.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        mainPanel.add(new JLabel("User Dashboard - Select an option from the menu", JLabel.CENTER), BorderLayout.CENTER);

        add(mainPanel);
    }

    private void openSearchTrains() {
        SearchTrainsFrame searchTrainsFrame = new SearchTrainsFrame(currentUser);
        searchTrainsFrame.setVisible(true);
    }

    private void openMyBookings() {
        MyBookingsFrame myBookingsFrame = new MyBookingsFrame(currentUser);
        myBookingsFrame.setVisible(true);
    }

    private void logout() {
        dispose();
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
}