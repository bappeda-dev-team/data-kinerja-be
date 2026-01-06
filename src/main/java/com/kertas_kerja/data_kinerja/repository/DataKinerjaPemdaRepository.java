package com.kertas_kerja.data_kinerja.repository;

import com.kertas_kerja.data_kinerja.model.DataKinerjaPemda;
import com.kertas_kerja.data_kinerja.model.Target;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class DataKinerjaPemdaRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public DataKinerjaPemda create(DataKinerjaPemda data) {
        String sql = "INSERT INTO tb_data_kinerja (jenis_data_id, nama_data, rumus_perhitungan, sumber_data, instansi_produsen_data, keterangan) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 1. Insert Parent
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, data.getJenisDataId());
            ps.setString(2, data.getNamaData());
            ps.setString(3, data.getRumusPerhitungan());
            ps.setString(4, data.getSumberData());
            ps.setString(5, data.getInstansiProdusenData());
            ps.setString(6, data.getKeterangan());
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys() != null && keyHolder.getKeys().get("id") != null) {
            data.setId(((Number) keyHolder.getKeys().get("id")).intValue());
        }

        // 2. Insert Targets
        if (data.getTarget() != null && !data.getTarget().isEmpty()) {
            String sqlTarget = "INSERT INTO tb_target (data_kinerja_id, target, satuan, tahun) VALUES (?, ?, ?, ?)";
            for (Target t : data.getTarget()) {
                jdbcTemplate.update(sqlTarget, data.getId(), t.getTarget(), t.getSatuan(), t.getTahun());
            }
        }

        return data;
    }

    @Transactional
    public DataKinerjaPemda update(DataKinerjaPemda data) {
        // 1. Update Parent
        String sql = "UPDATE tb_data_kinerja SET nama_data=?, rumus_perhitungan=?, sumber_data=?, instansi_produsen_data=?, keterangan=? WHERE id=?";
        jdbcTemplate.update(sql,
                data.getNamaData(),
                data.getRumusPerhitungan(),
                data.getSumberData(),
                data.getInstansiProdusenData(),
                data.getKeterangan(),
                data.getId());

        // 2. Sync Targets (Complex logic: Insert/Update/Delete)
        String sqlGetTargets = "SELECT * FROM tb_target WHERE data_kinerja_id = ?";
        List<Target> existingTargets = jdbcTemplate.query(sqlGetTargets, (rs, rowNum) -> Target.builder().id(rs.getInt("id")).build(), data.getId());

        Set<Integer> existingIds = new HashSet<>();
        for(Target t : existingTargets) existingIds.add(t.getId());
        Set<Integer> keptIds = new HashSet<>();

        if (data.getTarget() != null) {
            for (Target t : data.getTarget()) {
                if (t.getId() != null && existingIds.contains(t.getId())) {
                    keptIds.add(t.getId());
                    // Update existing target
                    jdbcTemplate.update("UPDATE tb_target SET target=?, satuan=?, tahun=? WHERE id=? AND data_kinerja_id=?",
                            t.getTarget(), t.getSatuan(), t.getTahun(), t.getId(), data.getId());
                } else {
                    // Insert new target
                    jdbcTemplate.update("INSERT INTO tb_target (data_kinerja_id, target, satuan, tahun) VALUES (?, ?, ?, ?)",
                            data.getId(), t.getTarget(), t.getSatuan(), t.getTahun());
                }
            }
        }

        // Delete removed targets
        for (Integer id : existingIds) {
            if (!keptIds.contains(id)) {
                jdbcTemplate.update("DELETE FROM tb_target WHERE id=? AND data_kinerja_id=?", id, data.getId());
            }
        }

        return findById(data.getId()).orElse(data);
    }

    public void delete(int id) {
        int rows = jdbcTemplate.update("DELETE FROM tb_data_kinerja WHERE id = ?", id);

        if (rows == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan");
        }
    }

    public Optional<DataKinerjaPemda> findById(int id) {
        String sql = """
            SELECT\s
                dk.id as dk_id,\s
                dk.jenis_data_id as jenis_data_id, dk.nama_data, dk.rumus_perhitungan,\s
                dk.sumber_data, dk.instansi_produsen_data, dk.keterangan,
                t.id as t_id, t.target, t.satuan, t.tahun,
                jd.jenis_data
            FROM tb_data_kinerja dk
            LEFT JOIN tb_target t ON dk.id = t.data_kinerja_id
            JOIN tb_jenis_data jd ON dk.jenis_data_id = jd.id
            WHERE dk.id = ?\s
            ORDER BY t.tahun DESC
       \s""";

        List<DataKinerjaPemda> results = jdbcTemplate.query(sql, new DataKinerjaPemdaExtractor(), id);
        assert results != null;
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<DataKinerjaPemda> findAll(Integer jenisDataId) {
        StringBuilder sql = new StringBuilder("""
            SELECT\s
                jd.id as jenis_data_id, jd.jenis_data,
                dk.id as dk_id,\s
                dk.nama_data, dk.rumus_perhitungan, dk.sumber_data,\s
                dk.instansi_produsen_data, dk.keterangan,
                t.id as t_id, t.target, t.satuan, t.tahun
            FROM tb_jenis_data jd
            LEFT JOIN tb_data_kinerja dk ON jd.id = dk.jenis_data_id
            LEFT JOIN tb_target t ON dk.id = t.data_kinerja_id
            WHERE 1=1
       \s""");

        List<Object> params = new ArrayList<>();
        if (jenisDataId != null && jenisDataId > 0) {
            sql.append(" AND jd.id = ?");
            params.add(jenisDataId);
        }

        sql.append(" ORDER BY jd.id ASC, dk.id ASC, t.tahun DESC");

        return jdbcTemplate.query(sql.toString(), new DataKinerjaPemdaExtractor(), params.toArray());
    }

    // --- INNER CLASS EXTRACTOR ---
    private static class DataKinerjaPemdaExtractor implements ResultSetExtractor<List<DataKinerjaPemda>> {
        @Override
        public List<DataKinerjaPemda> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, DataKinerjaPemda> map = new LinkedHashMap<>();

            while (rs.next()) {
                Integer dkId = (Integer) rs.getObject("dk_id");

                if (dkId != null) {
                    DataKinerjaPemda dk = map.get(dkId);
                    if (dk == null) {
                        dk = DataKinerjaPemda.builder()
                                .id(dkId)
                                .jenisDataId(rs.getInt("jenis_data_id"))
                                .jenisData(rs.getString("jenis_data"))
                                .namaData(rs.getString("nama_data"))
                                .rumusPerhitungan(rs.getString("rumus_perhitungan"))
                                .sumberData(rs.getString("sumber_data"))
                                .instansiProdusenData(rs.getString("instansi_produsen_data"))
                                .keterangan(rs.getString("keterangan"))
                                .target(new ArrayList<>()) // FIX: Init ArrayList agar tidak NPE di Service
                                .build();
                        map.put(dkId, dk);
                    }

                    // Map Child (Target)
                    Integer tId = (Integer) rs.getObject("t_id");
                    if (tId != null) {
                        Target t = Target.builder()
                                .id(tId)
                                .dataKinerjaId(dkId)
                                .target(rs.getString("target"))
                                .satuan(rs.getString("satuan"))
                                .tahun(rs.getString("tahun"))
                                .build();
                        dk.getTarget().add(t);
                    }
                }
            }
            return new ArrayList<>(map.values());
        }
    }
}