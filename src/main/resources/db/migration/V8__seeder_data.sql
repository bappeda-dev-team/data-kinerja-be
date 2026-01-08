-- ==========================================================
-- SEED DATA REAL (Tanpa embel-embel V8 di text)
-- ==========================================================

-- 1. SEED JENIS DATA (MASTER) -> tb_jenis_data
INSERT INTO tb_jenis_data (jenis_data)
VALUES ('Isu Strategis');

-- 2. SEED JENIS DATA OPD -> tb_jenis_data_opd
INSERT INTO tb_jenis_data_opd (kode_opd, nama_opd, jenis_data)
VALUES ('1.01.01', 'Dinas Pendidikan', 'Permasalahan');

-- 3. SEED DATA KINERJA PEMDA -> tb_data_kinerja
-- Relasi: tb_data_kinerja.jenis_data_id -> tb_jenis_data.id
INSERT INTO tb_data_kinerja (
    jenis_data_id,
    nama_data,
    rumus_perhitungan,
    sumber_data,
    instansi_produsen_data,
    keterangan
)
VALUES (
           (SELECT id FROM tb_jenis_data WHERE jenis_data = 'Isu Strategis' LIMIT 1),
           'Indeks Pembangunan Manusia',
           'Metode penghitungan standar IPM (UHH, HLS, RLS, Pengeluaran)',
           'BPS',
           'Bappeda',
           'Data Tahunan'
       );

-- 4. SEED TARGET KINERJA PEMDA -> tb_target
-- Relasi: tb_target.data_kinerja_id -> tb_data_kinerja.id
INSERT INTO tb_target (data_kinerja_id, target, satuan, tahun)
VALUES
    (
        (SELECT id FROM tb_data_kinerja WHERE nama_data = 'Indeks Pembangunan Manusia' LIMIT 1),
        '75.0', 'Poin', '2024'
    ),
    (
        (SELECT id FROM tb_data_kinerja WHERE nama_data = 'Indeks Pembangunan Manusia' LIMIT 1),
        '76.5', 'Poin', '2025'
    );

-- 5. SEED DATA KINERJA OPD -> tb_data_kinerja_opd
-- Relasi: tb_data_kinerja_opd.jenis_data_id -> tb_jenis_data_opd.id
INSERT INTO tb_data_kinerja_opd (
    jenis_data_id,
    kode_opd,
    nama_data,
    rumus_perhitungan,
    sumber_data,
    instansi_produsen_data,
    keterangan
)
VALUES (
           (SELECT id FROM tb_jenis_data_opd WHERE kode_opd = '1.01.01' LIMIT 1),
           '1.01.01',
           'Angka Partisipasi Murni (APM) SD',
           'Jumlah Siswa Usia 7-12 th di SD dibagi Penduduk Usia 7-12 th',
           'Dapodik',
           'Dinas Pendidikan',
           'Laporan Tahunan'
       );

-- 6. SEED TARGET KINERJA OPD -> tb_target_opd
-- Relasi: tb_target_opd.data_kinerja_opd_id -> tb_data_kinerja_opd.id
INSERT INTO tb_target_opd (data_kinerja_opd_id, target, satuan, tahun)
VALUES
    (
        (SELECT id FROM tb_data_kinerja_opd WHERE nama_data = 'Angka Partisipasi Murni (APM) SD' LIMIT 1),
        '98', 'Persen', '2024'
    ),
    (
        (SELECT id FROM tb_data_kinerja_opd WHERE nama_data = 'Angka Partisipasi Murni (APM) SD' LIMIT 1),
        '99', 'Persen', '2025'
    );