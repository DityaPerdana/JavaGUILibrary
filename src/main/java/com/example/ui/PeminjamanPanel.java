package com.example.ui;

import com.example.dao.BukuDAO;
import com.example.dao.PeminjamanDAO;
import com.example.model.Buku;
import com.example.model.Peminjaman;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class PeminjamanPanel extends JPanel {
    
    private PeminjamanDAO peminjamanDAO = new PeminjamanDAO();
    private BukuDAO bukuDAO = new BukuDAO();
    
    private JTable tablePeminjaman;
    private DefaultTableModel tableModel;
    
    private JTextField fieldId;
    private JTextField fieldNamaPeminjam;
    private JDateChooser dateChooserPinjam;
    private JDateChooser dateChooserKembali;
    private JComboBox<Buku> comboBuku;
    private JTextField fieldCari;
    
    public PeminjamanPanel() {
        setLayout(new BorderLayout());
        
        initComponents();
        loadPeminjamanData();
    }
    
    private void initComponents() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"ID", "Nama Peminjam", "Tanggal Pinjam", "Tanggal Kembali", "ID Buku", "Judul Buku"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePeminjaman = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablePeminjaman);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari:"));
        fieldCari = new JTextField(20);
        JButton btnCari = new JButton("Cari");
        btnCari.addActionListener(e -> searchPeminjaman());
        searchPanel.add(fieldCari);
        searchPanel.add(btnCari);
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Data Peminjaman"));
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        fieldId = new JTextField(20);
        fieldId.setEditable(false);
        inputPanel.add(fieldId, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Nama Peminjam:"), gbc);
        gbc.gridx = 1;
        fieldNamaPeminjam = new JTextField(20);
        inputPanel.add(fieldNamaPeminjam, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Tanggal Pinjam:"), gbc);
        gbc.gridx = 1;
        dateChooserPinjam = new JDateChooser();
        dateChooserPinjam.setDate(new Date());
        inputPanel.add(dateChooserPinjam, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Tanggal Kembali:"), gbc);
        gbc.gridx = 1;
        dateChooserKembali = new JDateChooser();
        Date oneWeekLater = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        dateChooserKembali.setDate(oneWeekLater);
        inputPanel.add(dateChooserKembali, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Buku:"), gbc);
        gbc.gridx = 1;
        comboBuku = new JComboBox<>();
        loadBukuComboBox();
        inputPanel.add(comboBuku, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnTambah = new JButton("Tambah");
        btnTambah.addActionListener(e -> tambahPeminjaman());
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> updatePeminjaman());
        
        JButton btnHapus = new JButton("Hapus");
        btnHapus.addActionListener(e -> hapusPeminjaman());
        
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> resetForm());
        
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(e -> {
            loadBukuComboBox();
            loadPeminjamanData();
        });
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnRefresh);
        
        formPanel.add(inputPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        tablePeminjaman.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablePeminjaman.getSelectedRow() != -1) {
                int selectedRow = tablePeminjaman.getSelectedRow();
                
                String id = tableModel.getValueAt(selectedRow, 0).toString();
                String namaPeminjam = tableModel.getValueAt(selectedRow, 1).toString();
                String tanggalPinjam = tableModel.getValueAt(selectedRow, 2).toString();
                String tanggalKembali = tableModel.getValueAt(selectedRow, 3).toString();
                String idBuku = tableModel.getValueAt(selectedRow, 4).toString();
                
                fieldId.setText(id);
                fieldNamaPeminjam.setText(namaPeminjam);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    dateChooserPinjam.setDate(sdf.parse(tanggalPinjam));
                    dateChooserKembali.setDate(sdf.parse(tanggalKembali));
                } catch (Exception ex) {
                    System.err.println("Error parsing date: " + ex.getMessage());
                }
                
                for (int i = 0; i < comboBuku.getItemCount(); i++) {
                    Buku buku = (Buku) comboBuku.getItemAt(i);
                    if (buku.getId().equals(idBuku)) {
                        comboBuku.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, formPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void loadBukuComboBox() {
        comboBuku.removeAllItems();
        
        List<Buku> daftarBuku = bukuDAO.getAllBuku();
        for (Buku buku : daftarBuku) {
            comboBuku.addItem(buku);
        }
    }
    
    private void loadPeminjamanData() {
        tableModel.setRowCount(0);
        
        List<Peminjaman> daftarPeminjaman = peminjamanDAO.getAllPeminjaman();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Peminjaman peminjaman : daftarPeminjaman) {
            Object[] rowData = {
                peminjaman.getId(),
                peminjaman.getNamaPeminjam(),
                sdf.format(peminjaman.getTanggalPinjam()),
                sdf.format(peminjaman.getTanggalKembali()),
                peminjaman.getIdBuku(),
                peminjaman.getJudulBuku()
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void tambahPeminjaman() {
        if (!validateFields()) {
            return;
        }
        
        Peminjaman peminjaman = new Peminjaman();
        peminjaman.setNamaPeminjam(fieldNamaPeminjam.getText().trim());
        peminjaman.setTanggalPinjam(dateChooserPinjam.getDate());
        peminjaman.setTanggalKembali(dateChooserKembali.getDate());
        
        Buku selectedBuku = (Buku) comboBuku.getSelectedItem();
        if (selectedBuku != null) {
            peminjaman.setIdBuku(selectedBuku.getId());
            peminjaman.setJudulBuku(selectedBuku.getJudul());
        }
        
        if (peminjamanDAO.tambahPeminjaman(peminjaman)) {
            JOptionPane.showMessageDialog(this,
                "Peminjaman berhasil ditambahkan!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            loadPeminjamanData();
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal menambahkan peminjaman!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updatePeminjaman() {
        if (!validateFields()) {
            return;
        }
        
        if (tablePeminjaman.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih peminjaman yang akan diupdate terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Peminjaman peminjaman = new Peminjaman();
        peminjaman.setId(Integer.parseInt(fieldId.getText().trim()));
        peminjaman.setNamaPeminjam(fieldNamaPeminjam.getText().trim());
        peminjaman.setTanggalPinjam(dateChooserPinjam.getDate());
        peminjaman.setTanggalKembali(dateChooserKembali.getDate());
        
        Buku selectedBuku = (Buku) comboBuku.getSelectedItem();
        if (selectedBuku != null) {
            peminjaman.setIdBuku(selectedBuku.getId());
            peminjaman.setJudulBuku(selectedBuku.getJudul());
        }
        
        if (peminjamanDAO.updatePeminjaman(peminjaman)) {
            JOptionPane.showMessageDialog(this,
                "Peminjaman berhasil diupdate!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            loadPeminjamanData();
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal mengupdate peminjaman!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusPeminjaman() {
        if (tablePeminjaman.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih peminjaman yang akan dihapus terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = Integer.parseInt(fieldId.getText().trim());
        
        int option = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus peminjaman dengan ID " + id + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            if (peminjamanDAO.hapusPeminjaman(id)) {
                JOptionPane.showMessageDialog(this,
                    "Peminjaman berhasil dihapus!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                resetForm();
                loadPeminjamanData();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal menghapus peminjaman!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void searchPeminjaman() {
        String keyword = fieldCari.getText().trim();
        
        if (keyword.isEmpty()) {
            loadPeminjamanData();
            return;
        }
        
        tableModel.setRowCount(0);
        
        List<Peminjaman> hasilPencarian = peminjamanDAO.cariPeminjaman(keyword);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Peminjaman peminjaman : hasilPencarian) {
            Object[] rowData = {
                peminjaman.getId(),
                peminjaman.getNamaPeminjam(),
                sdf.format(peminjaman.getTanggalPinjam()),
                sdf.format(peminjaman.getTanggalKembali()),
                peminjaman.getIdBuku(),
                peminjaman.getJudulBuku()
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void resetForm() {
        fieldId.setText("");
        fieldNamaPeminjam.setText("");
        dateChooserPinjam.setDate(new Date());
        
        Date oneWeekLater = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        dateChooserKembali.setDate(oneWeekLater);
        
        if (comboBuku.getItemCount() > 0) {
            comboBuku.setSelectedIndex(0);
        }
        
        tablePeminjaman.clearSelection();
    }
    
    private boolean validateFields() {
        if (fieldNamaPeminjam.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nama Peminjam tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            fieldNamaPeminjam.requestFocus();
            return false;
        }
        
        if (dateChooserPinjam.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                "Tanggal Pinjam harus diisi!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            dateChooserPinjam.requestFocus();
            return false;
        }
        
        if (dateChooserKembali.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                "Tanggal Kembali harus diisi!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            dateChooserKembali.requestFocus();
            return false;
        }
        
        if (dateChooserKembali.getDate().before(dateChooserPinjam.getDate())) {
            JOptionPane.showMessageDialog(this,
                "Tanggal Kembali tidak boleh sebelum Tanggal Pinjam!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            dateChooserKembali.requestFocus();
            return false;
        }
        
        if (comboBuku.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                "Buku harus dipilih!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            comboBuku.requestFocus();
            return false;
        }
        
        return true;
    }
}