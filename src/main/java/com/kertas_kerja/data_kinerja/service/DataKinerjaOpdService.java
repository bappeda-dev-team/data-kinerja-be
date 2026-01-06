package com.kertas_kerja.data_kinerja.service;

import com.kertas_kerja.data_kinerja.model.DataKinerjaOpd;
import com.kertas_kerja.data_kinerja.model.JenisDataOpd;
import com.kertas_kerja.data_kinerja.model.TargetOpd;
import com.kertas_kerja.data_kinerja.dto.DataKinerjaOpdDto;
import com.kertas_kerja.data_kinerja.dto.TargetDto;
import com.kertas_kerja.data_kinerja.repository.DataKinerjaOpdRepository;
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
public class DataKinerjaOpdService {

    private final DataKinerjaOpdRepository repository;
    private final JenisDataRepository jenisDataRepository;

    @Transactional
    public DataKinerjaOpdDto.Response create(DataKinerjaOpdDto.CreateRequest request) {
        log.info("Creating Data Kinerja OPD, JenisDataID: {}, KodeOPD: {}", request.jenisDataId(), request.kodeOpd());

        // 1. Cari Jenis Data OPD
        JenisDataOpd jenisDataOpd = jenisDataRepository.findByIdOpd(request.jenisDataId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis Data OPD tidak ditemukan"));

        // 2. Validasi Kode OPD (Logic dari Golang)
        if (!jenisDataOpd.getKodeOpd().equals(request.kodeOpd())) {
            log.error("Validasi Gagal: Request OPD {} != Master OPD {}", request.kodeOpd(), jenisDataOpd.getKodeOpd());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kode OPD tidak sesuai dengan Jenis Data OPD yang dipilih");
        }

        DataKinerjaOpd model = DataKinerjaOpd.builder()
                .jenisDataId(request.jenisDataId())
                .jenisData(jenisDataOpd.getJenisData())
                .kodeOpd(request.kodeOpd())
                .namaOpd(jenisDataOpd.getNamaOpd())
                .namaData(request.namaData())
                .rumusPerhitungan(request.rumusPerhitungan())
                .sumberData(request.sumberData())
                .instansiProdusenData(request.instansiProdusenData())
                .keterangan(request.keterangan())
                .target(request.target().stream().map(t -> TargetOpd.builder()
                        .target(t.target())
                        .satuan(t.satuan())
                        .tahun(t.tahun())
                        .build()).collect(Collectors.toList()))
                .build();

        DataKinerjaOpd saved = repository.create(model);
        return mapToResponse(saved);
    }

    @Transactional
    public DataKinerjaOpdDto.Response update(DataKinerjaOpdDto.UpdateRequest request) {
        log.info("Updating Data Kinerja OPD ID: {}", request.id());

        // Cek Exist
        repository.findById(request.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));

        JenisDataOpd jenisDataOpd = jenisDataRepository.findByIdOpd(request.jenisDataId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis Data OPD tidak ditemukan"));

        // Validasi Kode OPD
        if (!jenisDataOpd.getKodeOpd().equals(request.kodeOpd())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kode OPD tidak sesuai dengan Jenis Data OPD yang dipilih");
        }

        DataKinerjaOpd model = DataKinerjaOpd.builder()
                .id(request.id())
                .jenisDataId(request.jenisDataId())
                .jenisData(jenisDataOpd.getJenisData())
                .kodeOpd(request.kodeOpd())
                .namaOpd(jenisDataOpd.getNamaOpd())
                .namaData(request.namaData())
                .rumusPerhitungan(request.rumusPerhitungan())
                .sumberData(request.sumberData())
                .instansiProdusenData(request.instansiProdusenData())
                .keterangan(request.keterangan())
                .target(request.target().stream().map(t -> TargetOpd.builder()
                        .id(t.id())
                        .dataKinerjaOpdId(request.id())
                        .target(t.target())
                        .satuan(t.satuan())
                        .tahun(t.tahun())
                        .build()).collect(Collectors.toList()))
                .build();

        DataKinerjaOpd updated = repository.update(model);
        return mapToResponse(updated);
    }

    @Transactional
    public void delete(int id) {
        repository.delete(id);
    }

    public DataKinerjaOpdDto.Response findById(int id) {
        DataKinerjaOpd result = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));
        return mapToResponse(result);
    }

    public List<DataKinerjaOpdDto.WrapperResponse> findAll(String kodeOpd, Integer jenisDataId) {
        List<DataKinerjaOpd> results = repository.findAll(kodeOpd, jenisDataId);

        if (results.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Integer, DataKinerjaOpdDto.WrapperResponse> groupedMap = new LinkedHashMap<>();

        for (DataKinerjaOpd item : results) {
            groupedMap.computeIfAbsent(item.getJenisDataId(), k -> DataKinerjaOpdDto.WrapperResponse.builder()
                    .id(item.getJenisDataId())
                    .jenisData(item.getJenisData())
                    .kodeOpd(item.getKodeOpd())
                    .namaOpd(item.getNamaOpd())
                    .dataKinerja(new ArrayList<>())
                    .build());

            if (item.getId() != null && item.getId() > 0) {
                groupedMap.get(item.getJenisDataId()).dataKinerja().add(mapToResponse(item));
            }
        }

        return new ArrayList<>(groupedMap.values());
    }

    // --- Helper Mapper ---
    private DataKinerjaOpdDto.Response mapToResponse(DataKinerjaOpd model) {
        return DataKinerjaOpdDto.Response.builder()
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