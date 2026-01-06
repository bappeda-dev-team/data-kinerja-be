package com.kertas_kerja.data_kinerja.service;

import com.kertas_kerja.data_kinerja.model.JenisData;
import com.kertas_kerja.data_kinerja.model.JenisDataOpd;
import com.kertas_kerja.data_kinerja.dto.JenisDataDto;
import com.kertas_kerja.data_kinerja.repository.JenisDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JenisDataService {

    private final JenisDataRepository repository;

    // ==========================================
    // MASTER JENIS DATA
    // ==========================================

    @Transactional
    public JenisDataDto.Response create(JenisDataDto.CreateRequest request) {
        JenisData model = JenisData.builder()
                .jenisData(request.jenisData())
                .build();

        model = repository.create(model);
        return mapToResponse(model);
    }

    @Transactional
    public JenisDataDto.Response update(JenisDataDto.UpdateRequest request) {
        repository.findById(request.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));

        JenisData model = JenisData.builder()
                .id(request.id())
                .jenisData(request.jenisData())
                .build();

        model = repository.update(model);
        return mapToResponse(model);
    }

    @Transactional
    public void delete(int id) {
        repository.findById(id)
                .orElseThrow(()  -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));

        repository.delete(id);
    }

    public JenisDataDto.Response findById(int id) {
        JenisData model = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID tidak ditemukan"));
        return mapToResponse(model);
    }

    public List<JenisDataDto.Response> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==========================================
    // JENIS DATA OPD
    // ==========================================

    @Transactional
    public JenisDataDto.OpdResponse createOpd(JenisDataDto.OpdCreateRequest request) {
        JenisDataOpd model = JenisDataOpd.builder()
                .kodeOpd(request.kodeOpd())
                .namaOpd(request.namaOpd())
                .jenisData(request.jenisData())
                .build();

        model = repository.createOpd(model);
        return mapToOpdResponse(model);
    }

    @Transactional
    public JenisDataDto.OpdResponse updateOpd(JenisDataDto.OpdUpdateRequest request) {
        repository.findByIdOpd(request.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));

        JenisDataOpd model = JenisDataOpd.builder()
                .id(request.id())
                .kodeOpd(request.kodeOpd())
                .namaOpd(request.namaOpd())
                .jenisData(request.jenisData())
                .build();

        model = repository.updateOpd(model);
        return mapToOpdResponse(model);
    }

    @Transactional
    public void deleteOpd(int id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));

        repository.deleteOpd(id);
    }

    public JenisDataDto.OpdResponse findByIdOpd(int id) {
        JenisDataOpd model = repository.findByIdOpd(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID tidak ditemukan"));
        return mapToOpdResponse(model);
    }

    public List<JenisDataDto.OpdResponse> findAllOpd(String kodeOpd) {
        List<JenisDataOpd> result = repository.findAllOpd(kodeOpd);

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan");
        }

        return result.stream()
                .map(this::mapToOpdResponse)
                .collect(Collectors.toList());
    }

    // --- Helper Mappers ---

    private JenisDataDto.Response mapToResponse(JenisData model) {
        return JenisDataDto.Response.builder()
                .id(model.getId())
                .jenisData(model.getJenisData())
                .build();
    }

    private JenisDataDto.OpdResponse mapToOpdResponse(JenisDataOpd model) {
        return JenisDataDto.OpdResponse.builder()
                .id(model.getId())
                .kodeOpd(model.getKodeOpd())
                .namaOpd(model.getNamaOpd())
                .jenisData(model.getJenisData())
                .build();
    }
}