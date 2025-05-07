package com.example.ui;

import com.example.DatabaseConnection;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {
    
    private JTabbedPane tabbedPane;
    private BukuPanel bukuPanel;
    private PeminjamanPanel peminjamanPanel;
    
    public MainApp() {
        setTitle("Sistem Manajemen Perpustakaan");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        
        DatabaseConnection.initializeDatabase();
        
        setVisible(true);
    }
    
    private void initComponents() {
        tabbedPane = new JTabbedPane();
        
        bukuPanel = new BukuPanel();
        peminjamanPanel = new PeminjamanPanel();
        
        tabbedPane.addTab("Manajemen Buku", new ImageIcon(), bukuPanel, "Kelola data buku");
        tabbedPane.addTab("Peminjaman", new ImageIcon(), peminjamanPanel, "Kelola peminjaman buku");
        
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        setupMenu();
    }
    
    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Keluar");
        exitItem.addActionListener(e -> {
            DatabaseConnection.closeConnection();
            System.exit(0);
        });
        fileMenu.add(exitItem);
        
        JMenu helpMenu = new JMenu("Bantuan");
        JMenuItem aboutItem = new JMenuItem("Tentang");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Sistem Manajemen Perpustakaan\n" +
                "Versi 1.0\n" +
                "© 2025",
                "Tentang Aplikasi",
                JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
}