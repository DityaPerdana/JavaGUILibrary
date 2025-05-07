package com.example.ui;

import com.example.dao.BukuDAO;
import com.example.model.Buku;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BukuPanel extends JPanel {
    
    private BukuDAO bukuDAO = new BukuDAO();
    private JTable tableBuku;
    private DefaultTableModel tableModel;
    
    private JTextField fieldId;
    private JTextField fieldJudul;
    private JTextField fieldPengarang;
    private JTextField fieldTahunTerbit;
    private JComboBox<String> comboKategori;
    private JTextField fieldCari;
    
    public BukuPanel() {
        setLayout(new BorderLayout());
        
        initComponents();
        loadBukuData();
    }
    
    private void initComponents() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"ID", "Judul", "Pengarang", "Tahun Terbit", "Kategori"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableBuku = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableBuku);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari:"));
        fieldCari = new JTextField(20);
        JButton btnCari = new JButton("Cari");
        btnCari.addActionListener(e -> searchBuku());
        searchPanel.add(fieldCari);
        searchPanel.add(btnCari);
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Data Buku"));
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        fieldId = new JTextField(20);
        inputPanel.add(fieldId, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Judul:"), gbc);
        gbc.gridx = 1;
        fieldJudul = new JTextField(20);
        inputPanel.add(fieldJudul, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Pengarang:"), gbc);
        gbc.gridx = 1;
        fieldPengarang = new JTextField(20);
        inputPanel.add(fieldPengarang, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Tahun Terbit:"), gbc);
        gbc.gridx = 1;
        fieldTahunTerbit = new JTextField(20);
        inputPanel.add(fieldTahunTerbit, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Kategori:"), gbc);
        gbc.gridx = 1;
        String[] kategoriOptions = {"Novel", "Komik", "Ensiklopedia", "Biografi", "Lainnya"};
        comboKategori = new JComboBox<>(kategoriOptions);
        comboKategori.setSelectedIndex(0);
        inputPanel.add(comboKategori, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnTambah = new JButton("Tambah");
        btnTambah.addActionListener(e -> tambahBuku());
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> updateBuku());
        
        JButton btnHapus = new JButton("Hapus");
        btnHapus.addActionListener(e -> hapusBuku());
        
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> resetForm());
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);
        
        formPanel.add(inputPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        tableBuku.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableBuku.getSelectedRow() != -1) {
                int selectedRow = tableBuku.getSelectedRow();
                String id = tableModel.getValueAt(selectedRow, 0).toString();
                String judul = tableModel.getValueAt(selectedRow, 1).toString();
                String pengarang = tableModel.getValueAt(selectedRow, 2).toString();
                String tahunTerbit = tableModel.getValueAt(selectedRow, 3).toString();
                String kategori = tableModel.getValueAt(selectedRow, 4).toString();
                
                fieldId.setText(id);
                fieldJudul.setText(judul);
                fieldPengarang.setText(pengarang);
                fieldTahunTerbit.setText(tahunTerbit);
                comboKategori.setSelectedItem(kategori);
                
                fieldId.setEditable(false);
            }
        });
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, formPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void loadBukuData() {
        tableModel.setRowCount(0);
        
        List<Buku> daftarBuku = bukuDAO.getAllBuku();
        
        for (Buku buku : daftarBuku) {
            Object[] rowData = {
                buku.getId(),
                buku.getJudul(),
                buku.getPengarang(),
                buku.getTahunTerbit(),
                buku.getKategori()
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void tambahBuku() {
        if (!validateFields()) {
            return;
        }
        
        String id = fieldId.getText().trim();
        
        if (!bukuDAO.isIdTersedia(id)) {
            JOptionPane.showMessageDialog(this,
                "ID Buku " + id + " sudah digunakan!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Buku buku = new Buku();
        buku.setId(id);
        buku.setJudul(fieldJudul.getText().trim());
        buku.setPengarang(fieldPengarang.getText().trim());
        buku.setTahunTerbit(Integer.parseInt(fieldTahunTerbit.getText().trim()));
        buku.setKategori(comboKategori.getSelectedItem().toString());
        
        if (bukuDAO.tambahBuku(buku)) {
            JOptionPane.showMessageDialog(this,
                "Buku berhasil ditambahkan!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            loadBukuData();
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal menambahkan buku!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateBuku() {
        if (!validateFields()) {
            return;
        }
        
        if (tableBuku.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih buku yang akan diupdate terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Buku buku = new Buku();
        buku.setId(fieldId.getText().trim());
        buku.setJudul(fieldJudul.getText().trim());
        buku.setPengarang(fieldPengarang.getText().trim());
        buku.setTahunTerbit(Integer.parseInt(fieldTahunTerbit.getText().trim()));
        buku.setKategori(comboKategori.getSelectedItem().toString());
        
        if (bukuDAO.updateBuku(buku)) {
            JOptionPane.showMessageDialog(this,
                "Buku berhasil diupdate!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            loadBukuData();
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal mengupdate buku!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusBuku() {
        if (tableBuku.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih buku yang akan dihapus terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = fieldId.getText().trim();
        
        int option = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus buku dengan ID " + id + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            if (bukuDAO.hapusBuku(id)) {
                JOptionPane.showMessageDialog(this,
                    "Buku berhasil dihapus!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                resetForm();
                loadBukuData();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal menghapus buku! Periksa apakah buku sedang dipinjam.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void searchBuku() {
        String keyword = fieldCari.getText().trim();
        
        if (keyword.isEmpty()) {
            loadBukuData();
            return;
        }
        
        tableModel.setRowCount(0);
        
        List<Buku> hasilPencarian = bukuDAO.cariBuku(keyword);
        
        for (Buku buku : hasilPencarian) {
            Object[] rowData = {
                buku.getId(),
                buku.getJudul(),
                buku.getPengarang(),
                buku.getTahunTerbit(),
                buku.getKategori()
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void resetForm() {
        fieldId.setText("");
        fieldJudul.setText("");
        fieldPengarang.setText("");
        fieldTahunTerbit.setText("");
        comboKategori.setSelectedIndex(0);
        
        tableBuku.clearSelection();
        fieldId.setEditable(true);
    }
    
    private boolean validateFields() {
        if (fieldId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "ID Buku tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            fieldId.requestFocus();
            return false;
        }
        
        if (fieldJudul.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Judul Buku tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            fieldJudul.requestFocus();
            return false;
        }
        
        if (fieldPengarang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nama Pengarang tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            fieldPengarang.requestFocus();
            return false;
        }
        
        try {
            int tahun = Integer.parseInt(fieldTahunTerbit.getText().trim());
            if (tahun < 1000 || tahun > 2100) {
                throw new NumberFormatException("Tahun harus antara 1000-2100");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Tahun Terbit harus berupa angka valid (1000-2100)!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            fieldTahunTerbit.requestFocus();
            return false;
        }
        
        return true;
    }
}