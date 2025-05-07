package com.example.dao;

import com.example.DatabaseConnection;
import com.example.model.Buku;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {
    
    public boolean tambahBuku(Buku buku) {
        String sql = "INSERT INTO buku (id, judul, pengarang, tahun_terbit, kategori) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, buku.getId());
            stmt.setString(2, buku.getJudul());
            stmt.setString(3, buku.getPengarang());
            stmt.setInt(4, buku.getTahunTerbit());
            stmt.setString(5, buku.getKategori());
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            System.err.println("Error menambah buku: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateBuku(Buku buku) {
        String sql = "UPDATE buku SET judul = ?, pengarang = ?, tahun_terbit = ?, kategori = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, buku.getJudul());
            stmt.setString(2, buku.getPengarang());
            stmt.setInt(3, buku.getTahunTerbit());
            stmt.setString(4, buku.getKategori());
            stmt.setString(5, buku.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            System.err.println("Error mengupdate buku: " + e.getMessage());
            return false;
        }
    }
    
    public boolean hapusBuku(String id) {
        String sql = "DELETE FROM buku WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
            
        } catch (SQLException e) {
            System.err.println("Error menghapus buku: " + e.getMessage());
            return false;
        }
    }
    
    public Buku getBukuById(String id) {
        String sql = "SELECT * FROM buku WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Buku buku = new Buku();
                    buku.setId(rs.getString("id"));
                    buku.setJudul(rs.getString("judul"));
                    buku.setPengarang(rs.getString("pengarang"));
                    buku.setTahunTerbit(rs.getInt("tahun_terbit"));
                    buku.setKategori(rs.getString("kategori"));
                    return buku;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error mendapatkan buku: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Buku> getAllBuku() {
        List<Buku> daftarBuku = new ArrayList<>();
        String sql = "SELECT * FROM buku ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Buku buku = new Buku();
                buku.setId(rs.getString("id"));
                buku.setJudul(rs.getString("judul"));
                buku.setPengarang(rs.getString("pengarang"));
                buku.setTahunTerbit(rs.getInt("tahun_terbit"));
                buku.setKategori(rs.getString("kategori"));
                daftarBuku.add(buku);
            }
            
        } catch (SQLException e) {
            System.err.println("Error mengambil data buku: " + e.getMessage());
        }
        
        return daftarBuku;
    }
    
    public List<Buku> cariBuku(String keyword) {
        List<Buku> hasilPencarian = new ArrayList<>();
        String sql = "SELECT * FROM buku WHERE " +
                     "LOWER(id) LIKE ? OR " +
                     "LOWER(judul) LIKE ? OR " +
                     "LOWER(pengarang) LIKE ? OR " +
                     "LOWER(kategori) LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchKeyword = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);
            stmt.setString(4, searchKeyword);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Buku buku = new Buku();
                    buku.setId(rs.getString("id"));
                    buku.setJudul(rs.getString("judul"));
                    buku.setPengarang(rs.getString("pengarang"));
                    buku.setTahunTerbit(rs.getInt("tahun_terbit"));
                    buku.setKategori(rs.getString("kategori"));
                    hasilPencarian.add(buku);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error mencari buku: " + e.getMessage());
        }
        
        return hasilPencarian;
    }
    
    public boolean isIdTersedia(String id) {
        String sql = "SELECT COUNT(*) FROM buku WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error memeriksa id buku: " + e.getMessage());
        }
        
        return false;
    }
}