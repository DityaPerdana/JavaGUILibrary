package com.example;

import com.example.ui.MainApp;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            new MainApp();
        });
    }
}