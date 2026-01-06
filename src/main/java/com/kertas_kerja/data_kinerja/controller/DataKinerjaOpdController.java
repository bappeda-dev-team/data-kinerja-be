package com.kertas_kerja.data_kinerja.controller;

import com.kertas_kerja.data_kinerja.dto.ApiResponse;
import com.kertas_kerja.data_kinerja.dto.DataKinerjaOpdDto;
import com.kertas_kerja.data_kinerja.service.DataKinerjaOpdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datakinerjaopd")
@RequiredArgsConstructor
@Tag(name = "4. Data Kinerja OPD", description = "Manajemen Data Kinerja Spesifik Organisasi Perangkat Daerah")
public class DataKinerjaOpdController {

    private final DataKinerjaOpdService service;

    @PostMapping
    public ResponseEntity<ApiResponse<DataKinerjaOpdDto.Response>> create(@Valid @RequestBody DataKinerjaOpdDto.CreateRequest request) {
        DataKinerjaOpdDto.Response response = service.create(request);
        return ResponseEntity.status(201).body(ApiResponse.created(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DataKinerjaOpdDto.Response>> update(@PathVariable Integer id, @Valid @RequestBody DataKinerjaOpdDto.UpdateRequest request) {
        DataKinerjaOpdDto.UpdateRequest requestWithId = new DataKinerjaOpdDto.UpdateRequest(
                id,
                request.jenisDataId(),
                request.kodeOpd(),
                request.namaData(),
                request.rumusPerhitungan(),
                request.sumberData(),
                request.instansiProdusenData(),
                request.keterangan(),
                request.target()
        );

        DataKinerjaOpdDto.Response response = service.update(requestWithId);
        return ResponseEntity.ok(ApiResponse.updated(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponse<DataKinerjaOpdDto.Response>> findById(@PathVariable Integer id) {
        DataKinerjaOpdDto.Response response = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "OK"));
    }

    @GetMapping("/list/{kodeOpd}")
    public ResponseEntity<ApiResponse<List<DataKinerjaOpdDto.WrapperResponse>>> findAll(
            @PathVariable String kodeOpd,
            @RequestParam(name = "jenis_data_id", required = false) Integer jenisDataId
    ) {
        List<DataKinerjaOpdDto.WrapperResponse> responses = service.findAll(kodeOpd, jenisDataId);
        return ResponseEntity.ok(ApiResponse.success(responses, "OK"));
    }
}