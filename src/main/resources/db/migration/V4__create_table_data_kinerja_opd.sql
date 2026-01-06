CREATE TABLE tb_data_kinerja_opd
(
    id                     SERIAL PRIMARY KEY,
    kode_opd               VARCHAR(255),
    nama_opd               VARCHAR(255),
    jenis_data_id          INT          NOT NULL,
    nama_data              TEXT,
    rumus_perhitungan      TEXT,
    sumber_data            TEXT,
    instansi_produsen_data VARCHAR(255) NOT NULL,
    keterangan             TEXT,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tb_data_kinerja_opd_jenis_data_opd FOREIGN KEY (jenis_data_id)
        REFERENCES tb_jenis_data_opd (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);