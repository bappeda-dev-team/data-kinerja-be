-- ==========================================
-- 1. SEED JENIS DATA (MASTER) - Tambah 2 Kategori
-- ==========================================
INSERT INTO tb_jenis_data (jenis_data) VALUES
                                           ('Isu Strategis Ekonomi'),
                                           ('Isu Strategis Lingkungan Hidup');

-- ==========================================
-- 2. SEED DATA KINERJA PEMDA (2 Indikator per Kategori)
-- ==========================================

-- A. Data untuk Isu Ekonomi
INSERT INTO tb_data_kinerja (
    jenis_data_id, nama_data, rumus_perhitungan, sumber_data, instansi_produsen_data, keterangan
) VALUES
      (
          (SELECT id FROM tb_jenis_data WHERE jenis_data = 'Isu Strategis Ekonomi' LIMIT 1),
          'Laju Pertumbuhan Ekonomi (LPE)',
          'PDB tahun berjalan dikurang PDB tahun lalu dibagi PDB tahun lalu',
          'BPS',
          'Bappeda Bidang Ekonomi',
          'Data Makro Tahunan'
      ),
      (
          (SELECT id FROM tb_jenis_data WHERE jenis_data = 'Isu Strategis Ekonomi' LIMIT 1),
          'Tingkat Pengangguran Terbuka (TPT)',
          'Jumlah penganggur dibagi angkatan kerja dikali 100 persen',
          'BPS / Sakernas',
          'Dinas Tenaga Kerja',
          'Data Semesteran'
      );

-- B. Data untuk Isu Lingkungan
INSERT INTO tb_data_kinerja (
    jenis_data_id, nama_data, rumus_perhitungan, sumber_data, instansi_produsen_data, keterangan
) VALUES
      (
          (SELECT id FROM tb_jenis_data WHERE jenis_data = 'Isu Strategis Lingkungan Hidup' LIMIT 1),
          'Indeks Kualitas Lingkungan Hidup (IKLH)',
          'Gabungan IKA, IKU, dan IKTL',
          'Dinas Lingkungan Hidup',
          'DLH Provinsi',
          'Laporan Status Lingkungan Hidup'
      ),
      (
          (SELECT id FROM tb_jenis_data WHERE jenis_data = 'Isu Strategis Lingkungan Hidup' LIMIT 1),
          'Persentase Penanganan Sampah',
          'Sampah terangkut dibagi timbulan sampah',
          'Dinas Lingkungan Hidup',
          'DLH Kabupaten',
          'Laporan Kebersihan'
      );

-- ==========================================
-- 3. SEED TARGET KINERJA PEMDA (Target 3 Tahun: 2024, 2025, 2026)
-- ==========================================

-- Target LPE
INSERT INTO tb_target (data_kinerja_id, target, satuan, tahun) VALUES
                                                                   ((SELECT id FROM tb_data_kinerja WHERE nama_data = 'Laju Pertumbuhan Ekonomi (LPE)' LIMIT 1), '5.2', 'Persen', '2024'),
                                                                   ((SELECT id FROM tb_data_kinerja WHERE nama_data = 'Laju Pertumbuhan Ekonomi (LPE)' LIMIT 1), '5.5', 'Persen', '2025'),
                                                                   ((SELECT id FROM tb_data_kinerja WHERE nama_data = 'Laju Pertumbuhan Ekonomi (LPE)' LIMIT 1), '5.8', 'Persen', '2026');

-- Target TPT (Semakin kecil semakin bagus)
INSERT INTO tb_target (data_kinerja_id, target, satuan, tahun) VALUES
                                                                   ((SELECT id FROM tb_data_kinerja WHERE nama_data = 'Tingkat Pengangguran Terbuka (TPT)' LIMIT 1), '4.5', 'Persen', '2024'),
                                                                   ((SELECT id FROM tb_data_kinerja WHERE nama_data = 'Tingkat Pengangguran Terbuka (TPT)' LIMIT 1), '4.2', 'Persen', '2025'),
                                                                   ((SELECT id FROM tb_data_kinerja WHERE nama_data = 'Tingkat Pengangguran Terbuka (TPT)' LIMIT 1), '4.0', 'Persen', '2026');

-- Target IKLH
INSERT INTO tb_target (data_kinerja_id, target, satuan, tahun) VALUES
                                                                   ((SELECT id FROM tb_data_kinerja WHERE nama_data = 'Indeks Kualitas Lingkungan Hidup (IKLH)' LIMIT 1), '68.0', 'Poin', '2024'),
                                                                   ((SELECT id FROM tb_data_kinerja WHERE nama_data = 'Indeks Kualitas Lingkungan Hidup (IKLH)' LIMIT 1), '70.5', 'Poin', '2025');

-- Target Sampah
INSERT INTO tb_target (data_kinerja_id, target, satuan, tahun) VALUES
                                                                   ((SELECT id FROM tb_data_kinerja WHERE nama_data = 'Persentase Penanganan Sampah' LIMIT 1), '80', 'Persen', '2024'),
                                                                   ((SELECT id FROM tb_data_kinerja WHERE nama_data = 'Persentase Penanganan Sampah' LIMIT 1), '85', 'Persen', '2025');


-- ==========================================
-- 4. SEED JENIS DATA OPD (Tambah 2 OPD: Dinkes & Dinas PU)
-- ==========================================
INSERT INTO tb_jenis_data_opd (kode_opd, nama_opd, jenis_data) VALUES
                                                                   ('1.02.01', 'Dinas Kesehatan', 'Indikator Kesehatan Masyarakat'),
                                                                   ('1.03.01', 'Dinas Pekerjaan Umum', 'Infrastruktur Dasar');


-- ==========================================
-- 5. SEED DATA KINERJA OPD
-- ==========================================

-- A. Dinas Kesehatan
INSERT INTO tb_data_kinerja_opd (
    jenis_data_id, kode_opd, nama_data, rumus_perhitungan, sumber_data, instansi_produsen_data, keterangan
) VALUES
      (
          (SELECT id FROM tb_jenis_data_opd WHERE kode_opd = '1.02.01' LIMIT 1),
          '1.02.01',
          'Prevalensi Stunting pada Balita',
          'Jumlah balita pendek/sangat pendek dibagi total balita diukur',
          'E-PPGBM',
          'Dinas Kesehatan',
          'Data Prioritas Nasional'
      ),
      (
          (SELECT id FROM tb_jenis_data_opd WHERE kode_opd = '1.02.01' LIMIT 1),
          '1.02.01',
          'Cakupan Imunisasi Dasar Lengkap',
          'Bayi imunisasi lengkap dibagi total bayi usia 0-11 bulan',
          'Laporan Puskesmas',
          'Dinas Kesehatan',
          'SPM Kesehatan'
      );

-- B. Dinas PU
INSERT INTO tb_data_kinerja_opd (
    jenis_data_id, kode_opd, nama_data, rumus_perhitungan, sumber_data, instansi_produsen_data, keterangan
) VALUES
    (
        (SELECT id FROM tb_jenis_data_opd WHERE kode_opd = '1.03.01' LIMIT 1),
        '1.03.01',
        'Persentase Jalan Kondisi Mantap',
        'Panjang jalan kondisi baik + sedang dibagi total panjang jalan kabupaten',
        'Survey Kondisi Jalan',
        'Dinas PU Bina Marga',
        'Laporan Akhir Tahun'
    );

-- ==========================================
-- 6. SEED TARGET KINERJA OPD
-- ==========================================

-- Target Stunting (Target Turun)
INSERT INTO tb_target_opd (data_kinerja_opd_id, target, satuan, tahun) VALUES
                                                                           ((SELECT id FROM tb_data_kinerja_opd WHERE nama_data = 'Prevalensi Stunting pada Balita' LIMIT 1), '14.0', 'Persen', '2024'),
                                                                           ((SELECT id FROM tb_data_kinerja_opd WHERE nama_data = 'Prevalensi Stunting pada Balita' LIMIT 1), '12.5', 'Persen', '2025');

-- Target Imunisasi
INSERT INTO tb_target_opd (data_kinerja_opd_id, target, satuan, tahun) VALUES
                                                                           ((SELECT id FROM tb_data_kinerja_opd WHERE nama_data = 'Cakupan Imunisasi Dasar Lengkap' LIMIT 1), '100', 'Persen', '2024'),
                                                                           ((SELECT id FROM tb_data_kinerja_opd WHERE nama_data = 'Cakupan Imunisasi Dasar Lengkap' LIMIT 1), '100', 'Persen', '2025');

-- Target Jalan Mantap
INSERT INTO tb_target_opd (data_kinerja_opd_id, target, satuan, tahun) VALUES
                                                                           ((SELECT id FROM tb_data_kinerja_opd WHERE nama_data = 'Persentase Jalan Kondisi Mantap' LIMIT 1), '78.5', 'Persen', '2024'),
                                                                           ((SELECT id FROM tb_data_kinerja_opd WHERE nama_data = 'Persentase Jalan Kondisi Mantap' LIMIT 1), '82.0', 'Persen', '2025'),
                                                                           ((SELECT id FROM tb_data_kinerja_opd WHERE nama_data = 'Persentase Jalan Kondisi Mantap' LIMIT 1), '85.0', 'Persen', '2026');