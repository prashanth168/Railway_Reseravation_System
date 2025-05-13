package com.railway.ui.auth;

import com.railway.dao.UserDAO;
import com.railway.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JButton registerButton;
    private JButton cancelButton;
    private LoginFrame loginFrame;

    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        setTitle("Railway Reservation System - Register");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                loginFrame.setVisible(true);
            }
        });
    }

    private void layoutComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(emailField, gbc);

        // First Name
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(lastNameField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if username or email already exists
        UserDAO userDAO = new UserDAO();
        if (userDAO.getUserByUsername(username) != null) {
            JOptionPane.showMessageDialog(this, "Username already exists", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDAO.getUserByEmail(email) != null) {
            JOptionPane.showMessageDialog(this, "Email already registered", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create new user
        User newUser = new User(username, password, email, firstName, lastName);
        if (userDAO.createUser(newUser)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            loginFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}