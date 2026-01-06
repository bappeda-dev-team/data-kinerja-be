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
public class Target {
    private Integer id;
    private Integer dataKinerjaId;
    private String target;
    private String satuan;
    private String tahun;

    public static class Mapper implements RowMapper<Target> {
        @Override
        public Target mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Target.builder()
                    .id(rs.getInt("id"))
                    .dataKinerjaId(rs.getInt("data_kinerja_id"))
                    .target(rs.getString("target"))
                    .satuan(rs.getString("satuan"))
                    .tahun(rs.getString("tahun"))
                    .build();
        }
    }
}