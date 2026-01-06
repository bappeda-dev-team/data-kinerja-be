package com.kertas_kerja.data_kinerja.controller;

import com.kertas_kerja.data_kinerja.dto.ApiResponse;
import com.kertas_kerja.data_kinerja.dto.DataKinerjaPemdaDto;
import com.kertas_kerja.data_kinerja.service.DataKinerjaPemdaService;
import io.swagger.v3.oas.annotations.tags.Tag; // <--- Import ini
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datakinerjapemda")
@RequiredArgsConstructor
@Tag(name = "3. Data Kinerja Pemda", description = "Manajemen Data Kinerja Pemerintah Daerah") // <--- Cukup tempel di sini
public class DataKinerjaPemdaController {

    private final DataKinerjaPemdaService service;

    @PostMapping
    public ResponseEntity<ApiResponse<DataKinerjaPemdaDto.Response>> create(@Valid @RequestBody DataKinerjaPemdaDto.CreateRequest request) {
        DataKinerjaPemdaDto.Response response = service.create(request);
        return ResponseEntity.status(201).body(ApiResponse.created(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DataKinerjaPemdaDto.Response>> update(@PathVariable Integer id, @Valid @RequestBody DataKinerjaPemdaDto.UpdateRequest request) {
        DataKinerjaPemdaDto.UpdateRequest requestWithId = new DataKinerjaPemdaDto.UpdateRequest(
                id,
                request.jenisDataId(),
                request.namaData(),
                request.rumusPerhitungan(),
                request.sumberData(),
                request.instansiProdusenData(),
                request.keterangan(),
                request.target()
        );

        DataKinerjaPemdaDto.Response response = service.update(requestWithId);
        return ResponseEntity.ok(ApiResponse.updated(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponse<DataKinerjaPemdaDto.Response>> findById(@PathVariable Integer id) {
        DataKinerjaPemdaDto.Response response = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "OK"));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<DataKinerjaPemdaDto.WrapperResponse>>> findAll(
            @RequestParam(name = "jenis_data_id", required = false) Integer jenisDataId
    ) {
        List<DataKinerjaPemdaDto.WrapperResponse> responses = service.findAll(jenisDataId);
        return ResponseEntity.ok(ApiResponse.success(responses, "OK"));
    }
}