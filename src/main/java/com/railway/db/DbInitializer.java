package com.railway.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitializer {
    public static void initializeDatabase() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            
            // Create tables if they don't exist
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL UNIQUE, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "phone VARCHAR(20), " +
                    "address TEXT, " +
                    "user_type ENUM('admin', 'customer') DEFAULT 'customer', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            // Add other tables similarly...
            
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}