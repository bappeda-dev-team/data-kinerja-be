package com.kertas_kerja.data_kinerja.model;

import lombok.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JenisDataOpd {
    private Integer id;
    private String kodeOpd;
    private String namaOpd;
    private String jenisData;

    public static class Mapper implements RowMapper<JenisDataOpd> {
        @Override
        public JenisDataOpd mapRow(ResultSet rs, int rowNum) throws SQLException {
            return JenisDataOpd.builder()
                    .id(rs.getInt("id"))
                    .kodeOpd(rs.getString("kode_opd"))
                    .namaOpd(rs.getString("nama_opd"))
                    .jenisData(rs.getString("jenis_data"))
                    .build();
        }
    }
}