package com.kertas_kerja.data_kinerja.model;

import lombok.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataKinerjaPemda {
    private Integer id;
    private Integer jenisDataId;
    private String jenisData; // Field hasil JOIN
    private String namaData;
    private String rumusPerhitungan;
    private String sumberData;
    private String instansiProdusenData;
    private String keterangan;

    @Builder.Default
    private List<Target> target = new ArrayList<>();

    public static class Mapper implements RowMapper<DataKinerjaPemda> {
        @Override
        public DataKinerjaPemda mapRow(ResultSet rs, int rowNum) throws SQLException {
            return DataKinerjaPemda.builder()
                    .id(rs.getInt("id"))
                    .jenisDataId(rs.getInt("jenis_data_id"))
                    // Pastikan query SQL nanti men-select kolom jenis_data (via JOIN)
                    .jenisData(rs.getString("jenis_data"))
                    .namaData(rs.getString("nama_data"))
                    .rumusPerhitungan(rs.getString("rumus_perhitungan"))
                    .sumberData(rs.getString("sumber_data"))
                    .instansiProdusenData(rs.getString("instansi_produsen_data"))
                    .keterangan(rs.getString("keterangan"))
                    .target(new ArrayList<>()) // List di-init kosong dulu
                    .build();
        }
    }
}