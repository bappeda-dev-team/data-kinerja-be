package com.kertas_kerja.data_kinerja.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class JenisDataDto {

    // === MASTER JENIS DATA ===

    @Builder
    public record CreateRequest(
            @NotBlank @JsonProperty("jenis_data") String jenisData
    ) {}

    @Builder
    public record UpdateRequest(
            @NotNull @JsonProperty("id") Integer id,
            @NotBlank @JsonProperty("jenis_data") String jenisData
    ) {}

    @Builder
    public record Response(
            @JsonProperty("id") Integer id,
            @JsonProperty("jenis_data") String jenisData
    ) {}

    // === JENIS DATA OPD ===

    @Builder
    public record OpdCreateRequest(
            @NotBlank @JsonProperty("kode_opd") String kodeOpd,
            @NotBlank @JsonProperty("nama_opd") String namaOpd,
            @NotBlank @JsonProperty("jenis_data") String jenisData
    ) {}

    @Builder
    public record OpdUpdateRequest(
            @NotNull @JsonProperty("id") Integer id,
            @NotBlank @JsonProperty("kode_opd") String kodeOpd,
            @NotBlank @JsonProperty("nama_opd") String namaOpd,
            @NotBlank @JsonProperty("jenis_data") String jenisData
    ) {}

    @Builder
    public record OpdResponse(
            @JsonProperty("id") Integer id,
            @JsonProperty("kode_opd") String kodeOpd,
            @JsonProperty("nama_opd") String namaOpd,
            @JsonProperty("jenis_data") String jenisData
    ) {}
}