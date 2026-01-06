package com.kertas_kerja.data_kinerja.controller;

import com.kertas_kerja.data_kinerja.dto.ApiResponse;
import com.kertas_kerja.data_kinerja.dto.JenisDataDto;
import com.kertas_kerja.data_kinerja.service.JenisDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JenisDataController {

    private final JenisDataService service;

    // ==========================================
    // GRUP 1: MASTER JENIS DATA
    // ==========================================

    @Tag(name = "1. Master Jenis Data", description = "CRUD untuk Master Jenis Data")
    @PostMapping("/jenisdata")
    public ResponseEntity<ApiResponse<JenisDataDto.Response>> create(@Valid @RequestBody JenisDataDto.CreateRequest request) {
        JenisDataDto.Response response = service.create(request);
        return ResponseEntity.status(201).body(ApiResponse.created(response));
    }

    @Tag(name = "1. Master Jenis Data")
    @PutMapping("/jenisdata/{id}")
    public ResponseEntity<ApiResponse<JenisDataDto.Response>> update(@PathVariable Integer id, @Valid @RequestBody JenisDataDto.UpdateRequest request) {
        JenisDataDto.UpdateRequest requestWithId = new JenisDataDto.UpdateRequest(id, request.jenisData());
        JenisDataDto.Response response = service.update(requestWithId);
        return ResponseEntity.ok(ApiResponse.updated(response));
    }

    @Tag(name = "1. Master Jenis Data")
    @DeleteMapping("/jenisdata/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @Tag(name = "1. Master Jenis Data")
    @GetMapping("/jenisdata/{id}")
    public ResponseEntity<ApiResponse<JenisDataDto.Response>> findById(@PathVariable Integer id) {
        JenisDataDto.Response response = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "OK"));
    }

    @Tag(name = "1. Master Jenis Data")
    @GetMapping("/jenisdata")
    public ResponseEntity<ApiResponse<List<JenisDataDto.Response>>> findAll() {
        List<JenisDataDto.Response> responses = service.findAll();
        return ResponseEntity.ok(ApiResponse.success(responses, "OK"));
    }

    // ==========================================
    // GRUP 2: JENIS DATA OPD
    // ==========================================

    @Tag(name = "2. Jenis Data OPD", description = "CRUD untuk Jenis Data spesifik OPD")
    @PostMapping("/jenisdataopd")
    public ResponseEntity<ApiResponse<JenisDataDto.OpdResponse>> createOpd(@Valid @RequestBody JenisDataDto.OpdCreateRequest request) {
        JenisDataDto.OpdResponse response = service.createOpd(request);
        return ResponseEntity.status(201).body(ApiResponse.created(response));
    }

    @Tag(name = "2. Jenis Data OPD")
    @GetMapping("/jenisdataopd/detail/{id}")
    public ResponseEntity<ApiResponse<JenisDataDto.OpdResponse>> findByIdOpd(@PathVariable Integer id) {
        JenisDataDto.OpdResponse response = service.findByIdOpd(id);
        return ResponseEntity.ok(ApiResponse.success(response, "OK"));
    }

    @Tag(name = "2. Jenis Data OPD")
    @GetMapping("/jenisdataopd/list/{kodeOpd}")
    public ResponseEntity<ApiResponse<List<JenisDataDto.OpdResponse>>> findAllOpd(@PathVariable String kodeOpd) {
        List<JenisDataDto.OpdResponse> responses = service.findAllOpd(kodeOpd);
        return ResponseEntity.ok(ApiResponse.success(responses, "OK"));
    }

    @Tag(name = "2. Jenis Data OPD")
    @PutMapping("/jenisdataopd/{id}")
    public ResponseEntity<ApiResponse<JenisDataDto.OpdResponse>> updateOpd(@PathVariable Integer id, @Valid @RequestBody JenisDataDto.OpdUpdateRequest request) {
        JenisDataDto.OpdUpdateRequest requestWithId = new JenisDataDto.OpdUpdateRequest(id, request.kodeOpd(), request.namaOpd(), request.jenisData());
        JenisDataDto.OpdResponse response = service.updateOpd(requestWithId);
        return ResponseEntity.ok(ApiResponse.updated(response));
    }

    @Tag(name = "2. Jenis Data OPD")
    @DeleteMapping("/jenisdataopd/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOpd(@PathVariable Integer id) {
        service.deleteOpd(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}