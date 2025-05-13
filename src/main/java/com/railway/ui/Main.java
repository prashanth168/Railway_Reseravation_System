package com.railway.ui;

import com.railway.db.DbInitializer;
import com.railway.db.DatabaseConnection;
import javax.swing.*;
import com.railway.ui.auth.*;
import java.sql.*;


public class Main {
    public static void main(String[] args) {
        // Initialize database
        DbInitializer.initializeDatabase();

        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and display the login frame
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
