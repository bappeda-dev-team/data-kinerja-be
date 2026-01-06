package com.kertas_kerja.data_kinerja.repository;

import com.kertas_kerja.data_kinerja.model.DataKinerjaOpd;
import com.kertas_kerja.data_kinerja.model.TargetOpd;
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
import java.util.*;

@Repository
@RequiredArgsConstructor
public class DataKinerjaOpdRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public DataKinerjaOpd create(DataKinerjaOpd data) {
        String sql = """
            INSERT INTO tb_data_kinerja_opd 
            (jenis_data_id, kode_opd, nama_opd, nama_data, rumus_perhitungan, sumber_data, instansi_produsen_data, keterangan) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            // PERBAIKAN: Gunakan new String[]{"id"} agar Postgres hanya mengembalikan kolom ID
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, data.getJenisDataId());
            ps.setString(2, data.getKodeOpd());
            ps.setString(3, data.getNamaOpd());
            ps.setString(4, data.getNamaData());
            ps.setString(5, data.getRumusPerhitungan());
            ps.setString(6, data.getSumberData());
            ps.setString(7, data.getInstansiProdusenData());
            ps.setString(8, data.getKeterangan());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            data.setId(keyHolder.getKey().intValue());
        }

        if (data.getTarget() != null) {
            String sqlTarget = "INSERT INTO tb_target_opd (data_kinerja_opd_id, target, satuan, tahun) VALUES (?, ?, ?, ?)";
            for (TargetOpd t : data.getTarget()) {
                jdbcTemplate.update(sqlTarget, data.getId(), t.getTarget(), t.getSatuan(), t.getTahun());
            }
        }
        return data;
    }

    @Transactional
    public DataKinerjaOpd update(DataKinerjaOpd data) {
        String sql = """
            UPDATE tb_data_kinerja_opd SET 
            jenis_data_id=?, kode_opd=?, nama_opd=?, nama_data=?, rumus_perhitungan=?, sumber_data=?, instansi_produsen_data=?, keterangan=? 
            WHERE id=?
        """;

        jdbcTemplate.update(sql,
                data.getJenisDataId(),
                data.getKodeOpd(),
                data.getNamaOpd(),
                data.getNamaData(),
                data.getRumusPerhitungan(),
                data.getSumberData(),
                data.getInstansiProdusenData(),
                data.getKeterangan(),
                data.getId());

        // Update Targets Logic
        String sqlGetTargets = "SELECT * FROM tb_target_opd WHERE data_kinerja_opd_id = ?";
        List<TargetOpd> existingTargets = jdbcTemplate.query(sqlGetTargets, new TargetOpd.Mapper(), data.getId());

        Map<Integer, TargetOpd> existingMap = new HashMap<>();
        for (TargetOpd t : existingTargets) {
            existingMap.put(t.getId(), t);
        }
        Set<Integer> targetIdsToKeep = new HashSet<>();

        if (data.getTarget() != null) {
            for (TargetOpd t : data.getTarget()) {
                if (t.getId() != null && t.getId() > 0) {
                    targetIdsToKeep.add(t.getId());
                    String sqlUpdate = "UPDATE tb_target_opd SET target=?, satuan=?, tahun=? WHERE id=? AND data_kinerja_opd_id=?";
                    jdbcTemplate.update(sqlUpdate, t.getTarget(), t.getSatuan(), t.getTahun(), t.getId(), data.getId());
                } else {
                    String sqlInsert = "INSERT INTO tb_target_opd (data_kinerja_opd_id, target, satuan, tahun) VALUES (?, ?, ?, ?)";
                    jdbcTemplate.update(sqlInsert, data.getId(), t.getTarget(), t.getSatuan(), t.getTahun());
                }
            }
        }

        for (Integer id : existingMap.keySet()) {
            if (!targetIdsToKeep.contains(id)) {
                jdbcTemplate.update("DELETE FROM tb_target_opd WHERE id=? AND data_kinerja_opd_id=?", id, data.getId());
            }
        }

        return findById(data.getId()).orElse(data);
    }

    public void delete(int id) {
        String sql = "DELETE FROM tb_data_kinerja_opd WHERE id = ?";

        int rows = jdbcTemplate.update(sql, id);

        if (rows == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Data tidak ditemukan"
            );
        }
    }


    public Optional<DataKinerjaOpd> findById(int id) {
        String sql = """
        SELECT dk.*,
               t.id AS t_id,
               t.target,
               t.satuan,
               t.tahun,
               jd.jenis_data
        FROM tb_data_kinerja_opd dk
        LEFT JOIN tb_target_opd t
               ON dk.id = t.data_kinerja_opd_id
        JOIN tb_jenis_data_opd jd
               ON dk.jenis_data_id = jd.id
        WHERE dk.id = ?
        ORDER BY t.tahun DESC
    """;

        System.out.println("This is sql:" + sql);

        List<DataKinerjaOpd> results =
                jdbcTemplate.query(sql, new DataKinerjaOpdExtractor(), id);

        System.out.println("This is results:" + results);

        assert results != null;
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<DataKinerjaOpd> findAll(String kodeOpd, Integer jenisDataId) {
        StringBuilder sql = new StringBuilder("""
            SELECT\s
                jd.id as jenis_data_id, jd.jenis_data, jd.kode_opd, jd.nama_opd,
                dk.id, dk.nama_data, dk.rumus_perhitungan, dk.sumber_data, dk.instansi_produsen_data, dk.keterangan,
                t.id as t_id, t.target, t.satuan, t.tahun
            FROM tb_jenis_data_opd jd
            LEFT JOIN tb_data_kinerja_opd dk ON jd.id = dk.jenis_data_id AND jd.kode_opd = dk.kode_opd
            LEFT JOIN tb_target_opd t ON dk.id = t.data_kinerja_opd_id
            WHERE 1=1
       \s""");

        List<Object> params = new ArrayList<>();
        if (kodeOpd != null && !kodeOpd.isEmpty()) {
            sql.append(" AND jd.kode_opd = ?");
            params.add(kodeOpd);
        }
        if (jenisDataId != null && jenisDataId > 0) {
            sql.append(" AND jd.id = ?");
            params.add(jenisDataId);
        }

        sql.append(" ORDER BY jd.id ASC, dk.id ASC, t.tahun DESC");
        return jdbcTemplate.query(sql.toString(), new DataKinerjaOpdExtractor(), params.toArray());
    }

    // --- EXTRACTOR ---
    private static class DataKinerjaOpdExtractor implements ResultSetExtractor<List<DataKinerjaOpd>> {
        @Override
        public List<DataKinerjaOpd> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, DataKinerjaOpd> map = new LinkedHashMap<>();

            while (rs.next()) {
                Integer dkId = rs.getObject("id", Integer.class);
                if (dkId != null) {
                    DataKinerjaOpd dk = map.get(dkId);
                    if (dk == null) {
                        dk = DataKinerjaOpd.builder()
                                .id(dkId)
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
                        map.put(dkId, dk);
                    }
                    Integer tId = rs.getObject("t_id", Integer.class);
                    if (tId != null) {
                        TargetOpd t = TargetOpd.builder()
                                .id(tId)
                                .dataKinerjaOpdId(dkId)
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