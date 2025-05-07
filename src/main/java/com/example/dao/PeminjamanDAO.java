package com.example.dao;

import com.example.DatabaseConnection;
import com.example.model.Peminjaman;
import com.example.model.Buku;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanDAO {
    
    private BukuDAO bukuDAO = new BukuDAO();
    
    public boolean tambahPeminjaman(Peminjaman peminjaman) {
        String sql = "INSERT INTO peminjaman (nama_peminjam, tanggal_pinjam, tanggal_kembali, id_buku) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, peminjaman.getNamaPeminjam());
            stmt.setDate(2, new java.sql.Date(peminjaman.getTanggalPinjam().getTime()));
            stmt.setDate(3, new java.sql.Date(peminjaman.getTanggalKembali().getTime()));
            stmt.setString(4, peminjaman.getIdBuku());
            
            int rowsInserted = stmt.executeUpdate();
            
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        peminjaman.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error menambah peminjaman: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updatePeminjaman(Peminjaman peminjaman) {
        String sql = "UPDATE peminjaman SET nama_peminjam = ?, tanggal_pinjam = ?, " +
                     "tanggal_kembali = ?, id_buku = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, peminjaman.getNamaPeminjam());
            stmt.setDate(2, new java.sql.Date(peminjaman.getTanggalPinjam().getTime()));
            stmt.setDate(3, new java.sql.Date(peminjaman.getTanggalKembali().getTime()));
            stmt.setString(4, peminjaman.getIdBuku());
            stmt.setInt(5, peminjaman.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            System.err.println("Error mengupdate peminjaman: " + e.getMessage());
            return false;
        }
    }
    
    public boolean hapusPeminjaman(int id) {
        String sql = "DELETE FROM peminjaman WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
            
        } catch (SQLException e) {
            System.err.println("Error menghapus peminjaman: " + e.getMessage());
            return false;
        }
    }
    
    public Peminjaman getPeminjamanById(int id) {
        String sql = "SELECT * FROM peminjaman WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Peminjaman peminjaman = new Peminjaman();
                    peminjaman.setId(rs.getInt("id"));
                    peminjaman.setNamaPeminjam(rs.getString("nama_peminjam"));
                    peminjaman.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
                    peminjaman.setTanggalKembali(rs.getDate("tanggal_kembali"));
                    peminjaman.setIdBuku(rs.getString("id_buku"));
                    
                    Buku buku = bukuDAO.getBukuById(peminjaman.getIdBuku());
                    if (buku != null) {
                        peminjaman.setJudulBuku(buku.getJudul());
                    }
                    
                    return peminjaman;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error mendapatkan peminjaman: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Peminjaman> getAllPeminjaman() {
        List<Peminjaman> daftarPeminjaman = new ArrayList<>();
        String sql = "SELECT * FROM peminjaman ORDER BY tanggal_pinjam DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Peminjaman peminjaman = new Peminjaman();
                peminjaman.setId(rs.getInt("id"));
                peminjaman.setNamaPeminjam(rs.getString("nama_peminjam"));
                peminjaman.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
                peminjaman.setTanggalKembali(rs.getDate("tanggal_kembali"));
                peminjaman.setIdBuku(rs.getString("id_buku"));
                
                Buku buku = bukuDAO.getBukuById(peminjaman.getIdBuku());
                if (buku != null) {
                    peminjaman.setJudulBuku(buku.getJudul());
                }
                
                daftarPeminjaman.add(peminjaman);
            }
            
        } catch (SQLException e) {
            System.err.println("Error mengambil data peminjaman: " + e.getMessage());
        }
        
        return daftarPeminjaman;
    }
    
    public List<Peminjaman> cariPeminjaman(String keyword) {
        List<Peminjaman> hasilPencarian = new ArrayList<>();
        String sql = "SELECT p.* FROM peminjaman p " +
                     "JOIN buku b ON p.id_buku = b.id " +
                     "WHERE LOWER(p.nama_peminjam) LIKE ? OR " +
                     "LOWER(b.judul) LIKE ? OR " +
                     "LOWER(p.id_buku) LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchKeyword = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Peminjaman peminjaman = new Peminjaman();
                    peminjaman.setId(rs.getInt("id"));
                    peminjaman.setNamaPeminjam(rs.getString("nama_peminjam"));
                    peminjaman.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
                    peminjaman.setTanggalKembali(rs.getDate("tanggal_kembali"));
                    peminjaman.setIdBuku(rs.getString("id_buku"));
                    
                    Buku buku = bukuDAO.getBukuById(peminjaman.getIdBuku());
                    if (buku != null) {
                        peminjaman.setJudulBuku(buku.getJudul());
                    }
                    
                    hasilPencarian.add(peminjaman);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error mencari peminjaman: " + e.getMessage());
        }
        
        return hasilPencarian;
    }
}