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
public class JenisData {
    private Integer id;
    private String jenisData;

    public static class Mapper implements RowMapper<JenisData> {
        @Override
        public JenisData mapRow(ResultSet rs, int rowNum) throws SQLException {
            return JenisData.builder()
                    .id(rs.getInt("id"))
                    .jenisData(rs.getString("jenis_data"))
                    .build();
        }
    }
}