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
public class DataKinerjaOpd {
    private Integer id;
    private Integer jenisDataId;
    private String jenisData; // Field hasil JOIN
    private String kodeOpd;
    private String namaOpd;
    private String namaData;
    private String rumusPerhitungan;
    private String sumberData;
    private String instansiProdusenData;
    private String keterangan;

    @Builder.Default
    private List<TargetOpd> target = new ArrayList<>();

    public static class Mapper implements RowMapper<DataKinerjaOpd> {
        @Override
        public DataKinerjaOpd mapRow(ResultSet rs, int rowNum) throws SQLException {
            return DataKinerjaOpd.builder()
                    .id(rs.getInt("id"))
                    .jenisDataId(rs.getInt("jenis_data_id"))
                    .jenisData(rs.getString("jenis_data"))
                    .kodeOpd(rs.getString("kode_opd"))
                    .namaOpd(rs.getString("nama_opd"))
                    .namaData(rs.getString("nama_data"))
                    .rumusPerhitungan(rs.getString("rumus_perhitungan"))
                    .sumberData(rs.getString("sumber_data"))
                    .instansiProdusenData(rs.getString("instansi_produsen_data"))
                    .keterangan(rs.getString("keterangan"))
                    .target(new ArrayList<>())
                    .build();
        }
    }
}