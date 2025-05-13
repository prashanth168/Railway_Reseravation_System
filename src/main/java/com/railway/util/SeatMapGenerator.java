package com.railway.util;

import com.railway.model.Seat;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class SeatMapGenerator {
    public static JPanel generateSeatMap(List<Seat> seats, int rows, int cols) {
        JPanel seatMapPanel = new JPanel(new GridLayout(rows, cols, 5, 5));
        seatMapPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        for (Seat seat : seats) {
            JButton seatButton = new JButton(seat.getSeatNumber());
            seatButton.setPreferredSize(new Dimension(50, 30));
            
            // Style based on seat status and class
            if (seat.isBooked()) {
                seatButton.setBackground(Color.RED);
                seatButton.setEnabled(false);
            } else {
                switch (seat.getClassType()) {
                    case "first":
                        seatButton.setBackground(new Color(0, 100, 0)); // Dark green
                        break;
                    case "ac":
                        seatButton.setBackground(new Color(70, 130, 180)); // Steel blue
                        break;
                    case "sleeper":
                        seatButton.setBackground(new Color(139, 69, 19)); // Brown
                        break;
                    default: // second class
                        seatButton.setBackground(new Color(0, 0, 139)); // Dark blue
                }
            }
            
            seatButton.setForeground(Color.WHITE);
            seatButton.setOpaque(true);
            seatButton.setBorderPainted(false);
            
            seatMapPanel.add(seatButton);
        }
        
        // Fill remaining spaces with empty labels if needed
        int remaining = rows * cols - seats.size();
        for (int i = 0; i < remaining; i++) {
            seatMapPanel.add(new JLabel(""));
        }
        
        return seatMapPanel;
    }
    
    public static ImageIcon generateSeatMapImage(List<Seat> seats, int rows, int cols) {
        JPanel panel = generateSeatMap(seats, rows, cols);
        panel.setSize(600, 400);
        
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        panel.paint(g);
        g.dispose();
        
        return new ImageIcon(image);
    }
}