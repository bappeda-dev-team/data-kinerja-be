package com.kertas_kerja.data_kinerja.repository;

import com.kertas_kerja.data_kinerja.model.JenisData;
import com.kertas_kerja.data_kinerja.model.JenisDataOpd;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JenisDataRepository {

    private final JdbcTemplate jdbcTemplate;

    // ==========================================
    // MASTER JENIS DATA (tb_jenis_data)
    // ==========================================

    public JenisData create(JenisData jenisData) {
        String sql = "INSERT INTO tb_jenis_data (jenis_data) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, jenisData.getJenisData());
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys() != null && keyHolder.getKeys().get("id") != null) {
            jenisData.setId(((Number) keyHolder.getKeys().get("id")).intValue());
        }
        return jenisData;
    }

    public JenisData update(JenisData jenisData) {
        String sql = "UPDATE tb_jenis_data SET jenis_data = ? WHERE id = ?";
        jdbcTemplate.update(sql, jenisData.getJenisData(), jenisData.getId());
        return jenisData;
    }

    public void delete(int id) {
        String sql = "DELETE FROM tb_jenis_data WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<JenisData> findById(int id) {
        String sql = "SELECT id, jenis_data FROM tb_jenis_data WHERE id = ?";
        try {
            JenisData result = jdbcTemplate.queryForObject(sql, new JenisData.Mapper(), id);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<JenisData> findAll() {
        String sql = "SELECT id, jenis_data FROM tb_jenis_data ORDER BY id";
        return jdbcTemplate.query(sql, new JenisData.Mapper());
    }

    // ==========================================
    // JENIS DATA OPD (tb_jenis_data_opd)
    // ==========================================

    public JenisDataOpd createOpd(JenisDataOpd jenisDataOpd) {
        String sql = "INSERT INTO tb_jenis_data_opd (kode_opd, nama_opd, jenis_data) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, jenisDataOpd.getKodeOpd());
            ps.setString(2, jenisDataOpd.getNamaOpd());
            ps.setString(3, jenisDataOpd.getJenisData());
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys() != null && keyHolder.getKeys().get("id") != null) {
            jenisDataOpd.setId(((Number) keyHolder.getKeys().get("id")).intValue());
        }
        return jenisDataOpd;
    }

    public JenisDataOpd updateOpd(JenisDataOpd jenisDataOpd) {
        String sql = "UPDATE tb_jenis_data_opd SET kode_opd = ?, nama_opd = ?, jenis_data = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                jenisDataOpd.getKodeOpd(),
                jenisDataOpd.getNamaOpd(),
                jenisDataOpd.getJenisData(),
                jenisDataOpd.getId());
        return jenisDataOpd;
    }

    public void deleteOpd(int id) {
        String sql = "DELETE FROM tb_jenis_data_opd WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<JenisDataOpd> findByIdOpd(int id) {
        String sql = "SELECT id, kode_opd, nama_opd, jenis_data FROM tb_jenis_data_opd WHERE id = ?";
        try {
            JenisDataOpd result = jdbcTemplate.queryForObject(sql, new JenisDataOpd.Mapper(), id);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<JenisDataOpd> findAllOpd(String kodeOpd) {
        StringBuilder sql = new StringBuilder("SELECT id, kode_opd, nama_opd, jenis_data FROM tb_jenis_data_opd WHERE 1=1 ");

        if (kodeOpd != null && !kodeOpd.isEmpty()) {
            sql.append("AND kode_opd = ? ");
            sql.append("ORDER BY id ASC");
            return jdbcTemplate.query(sql.toString(), new JenisDataOpd.Mapper(), kodeOpd);
        }

        sql.append("ORDER BY id ASC");
        return jdbcTemplate.query(sql.toString(), new JenisDataOpd.Mapper());
    }
}