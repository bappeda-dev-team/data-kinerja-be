package com.kertas_kerja.data_kinerja.service;

import com.kertas_kerja.data_kinerja.model.DataKinerjaPemda;
import com.kertas_kerja.data_kinerja.model.JenisData;
import com.kertas_kerja.data_kinerja.model.Target;
import com.kertas_kerja.data_kinerja.dto.DataKinerjaPemdaDto;
import com.kertas_kerja.data_kinerja.dto.TargetDto;
import com.kertas_kerja.data_kinerja.repository.DataKinerjaPemdaRepository;
import com.kertas_kerja.data_kinerja.repository.JenisDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataKinerjaPemdaService {

    private final DataKinerjaPemdaRepository repository;
    private final JenisDataRepository jenisDataRepository;

    @Transactional
    public DataKinerjaPemdaDto.Response create(DataKinerjaPemdaDto.CreateRequest request) {
        log.info("Creating Data Kinerja Pemda with JenisDataId: {}", request.jenisDataId());

        JenisData jenisData = jenisDataRepository.findById(request.jenisDataId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis Data tidak ditemukan"));

        DataKinerjaPemda model = DataKinerjaPemda.builder()
                .jenisDataId(request.jenisDataId())
                .jenisData(jenisData.getJenisData())
                .namaData(request.namaData())
                .rumusPerhitungan(request.rumusPerhitungan())
                .sumberData(request.sumberData())
                .instansiProdusenData(request.instansiProdusenData())
                .keterangan(request.keterangan())
                .target(request.target().stream().map(t -> Target.builder()
                        .target(t.target())
                        .satuan(t.satuan())
                        .tahun(t.tahun())
                        .build()).collect(Collectors.toList()))
                .build();

        DataKinerjaPemda saved = repository.create(model);
        return mapToResponse(saved);
    }

    @Transactional
    public DataKinerjaPemdaDto.Response update(DataKinerjaPemdaDto.UpdateRequest request) {
        log.info("Updating Data Kinerja Pemda ID: {}", request.id());

        DataKinerjaPemda existing = repository.findById(request.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Kinerja tidak ditemukan"));

        JenisData jenisData = jenisDataRepository.findById(existing.getJenisDataId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis Data tidak ditemukan"));

        DataKinerjaPemda model = DataKinerjaPemda.builder()
                .id(request.id())
                .jenisDataId(existing.getJenisDataId())
                .jenisData(jenisData.getJenisData())
                .namaData(request.namaData())
                .rumusPerhitungan(request.rumusPerhitungan())
                .sumberData(request.sumberData())
                .instansiProdusenData(request.instansiProdusenData())
                .keterangan(request.keterangan())
                .target(request.target().stream().map(t -> Target.builder()
                        .id(t.id())
                        .dataKinerjaId(request.id())
                        .target(t.target())
                        .satuan(t.satuan())
                        .tahun(t.tahun())
                        .build()).collect(Collectors.toList()))
                .build();

        DataKinerjaPemda updated = repository.update(model);
        return mapToResponse(updated);
    }

    @Transactional
    public void delete(int id) {
        repository.delete(id);
    }

    public DataKinerjaPemdaDto.Response findById(int id) {
        DataKinerjaPemda result = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));

        return mapToResponse(result);
    }

    public List<DataKinerjaPemdaDto.WrapperResponse> findAll(Integer jenisDataId) {
        List<DataKinerjaPemda> results = repository.findAll(jenisDataId);

        if (results.isEmpty()) {
            return new ArrayList<>();
        }

        // Logic Grouping mirip Golang: Group by Jenis Data ID
        Map<Integer, DataKinerjaPemdaDto.WrapperResponse> groupedMap = new LinkedHashMap<>();

        for (DataKinerjaPemda item : results) {
            // Pastikan Wrapper untuk Jenis Data ini ada
            groupedMap.computeIfAbsent(item.getJenisDataId(), k -> DataKinerjaPemdaDto.WrapperResponse.builder()
                    .id(item.getJenisDataId())
                    .jenisData(item.getJenisData())
                    .dataKinerja(new ArrayList<>())
                    .build());

            // Tambahkan Data Kinerja ke List (Hanya jika ID valid > 0)
            if (item.getId() != null && item.getId() > 0) {
                groupedMap.get(item.getJenisDataId()).dataKinerja().add(mapToResponse(item));
            }
        }

        return new ArrayList<>(groupedMap.values());
    }

    // --- Helper Mapper ---
    private DataKinerjaPemdaDto.Response mapToResponse(DataKinerjaPemda model) {
        return DataKinerjaPemdaDto.Response.builder()
                .id(model.getId())
                .namaData(model.getNamaData())
                .rumusPerhitungan(model.getRumusPerhitungan())
                .sumberData(model.getSumberData())
                .instansiProdusenData(model.getInstansiProdusenData())
                .keterangan(model.getKeterangan())
                .target(model.getTarget().stream().map(t -> TargetDto.Response.builder()
                        .id(t.getId())
                        .target(t.getTarget())
                        .satuan(t.getSatuan())
                        .tahun(t.getTahun())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}