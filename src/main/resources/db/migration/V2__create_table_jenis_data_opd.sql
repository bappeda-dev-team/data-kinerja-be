CREATE TABLE tb_jenis_data_opd
(
    id         SERIAL PRIMARY KEY,
    kode_opd   VARCHAR(255),
    nama_opd   VARCHAR(255),
    jenis_data VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);