package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_NAME = "perpustakaan";
    private static final String BASE_URL = "jdbc:postgresql://localhost:5432/";
    private static final String URL = BASE_URL + DB_NAME;
    private static final String USER = "ditya";
    private static final String PASSWORD = ".";
    
    private static Connection connection;
    
    public static Connection getConnection() throws SQLException {
        ensureDatabaseExists();
        
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public static void ensureDatabaseExists() {
        try (Connection conn = DriverManager.getConnection(BASE_URL + "postgres", USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            ResultSet resultSet = stmt.executeQuery(
                    "SELECT 1 FROM pg_database WHERE datname = '" + DB_NAME + "'");
            
            if (!resultSet.next()) {
                stmt.execute("CREATE DATABASE " + DB_NAME);
                System.out.println("Database '" + DB_NAME + "' created successfully!");
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking/creating database: " + e.getMessage());
        }
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("CREATE TABLE IF NOT EXISTS buku (" +
                         "id VARCHAR(10) PRIMARY KEY, " +
                         "judul VARCHAR(100) NOT NULL, " +
                         "pengarang VARCHAR(100) NOT NULL, " +
                         "tahun_terbit INTEGER NOT NULL, " +
                         "kategori VARCHAR(50) NOT NULL)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS peminjaman (" +
                         "id SERIAL PRIMARY KEY, " +
                         "nama_peminjam VARCHAR(100) NOT NULL, " +
                         "tanggal_pinjam DATE NOT NULL, " +
                         "tanggal_kembali DATE NOT NULL, " +
                         "id_buku VARCHAR(10) NOT NULL, " +
                         "FOREIGN KEY (id_buku) REFERENCES buku(id))");
            
            System.out.println("Database tables initialized successfully!");
            
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
}