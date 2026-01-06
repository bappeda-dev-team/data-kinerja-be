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
public class TargetOpd {
    private Integer id;
    private Integer dataKinerjaOpdId;
    private String target;
    private String satuan;
    private String tahun;

    public static class Mapper implements RowMapper<TargetOpd> {
        @Override
        public TargetOpd mapRow(ResultSet rs, int rowNum) throws SQLException {
            return TargetOpd.builder()
                    .id(rs.getInt("id"))
                    .dataKinerjaOpdId(rs.getInt("data_kinerja_opd_id"))
                    .target(rs.getString("target"))
                    .satuan(rs.getString("satuan"))
                    .tahun(rs.getString("tahun"))
                    .build();
        }
    }
}