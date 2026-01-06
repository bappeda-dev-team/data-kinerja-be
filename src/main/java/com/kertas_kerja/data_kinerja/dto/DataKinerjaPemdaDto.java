package com.kertas_kerja.data_kinerja.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.util.List;

public class DataKinerjaPemdaDto {

    @Builder
    public record CreateRequest(
            @NotNull @JsonProperty("jenis_data_id") Integer jenisDataId,
            @NotBlank @JsonProperty("nama_data") String namaData,
            @NotBlank @JsonProperty("rumus_perhitungan") String rumusPerhitungan,
            @NotBlank @JsonProperty("sumber_data") String sumberData,
            @NotBlank @JsonProperty("instansi_produsen_data") String instansiProdusenData,
            @JsonProperty("keterangan") String keterangan,

            // Memanggil Record TargetDto.CreateRequest
            @NotNull @JsonProperty("target") List<TargetDto.CreateRequest> target
    ) {}

    @Builder
    public record UpdateRequest(
            @NotNull @JsonProperty("id") Integer id,
            @NotNull @JsonProperty("jenis_data_id") Integer jenisDataId,
            @NotBlank @JsonProperty("nama_data") String namaData,
            @NotBlank @JsonProperty("rumus_perhitungan") String rumusPerhitungan,
            @NotBlank @JsonProperty("sumber_data") String sumberData,
            @NotBlank @JsonProperty("instansi_produsen_data") String instansiProdusenData,
            @JsonProperty("keterangan") String keterangan,

            @NotNull @JsonProperty("target") List<TargetDto.UpdateRequest> target
    ) {}

    @Builder
    public record Response(
            @JsonProperty("id") Integer id,
            @JsonProperty("nama_data") String namaData,
            @JsonProperty("rumus_perhitungan") String rumusPerhitungan,
            @JsonProperty("sumber_data") String sumberData,
            @JsonProperty("instansi_produsen_data") String instansiProdusenData,
            @JsonProperty("keterangan") String keterangan,
            @JsonProperty("target") List<TargetDto.Response> target
    ) {}

    @Builder
    public record WrapperResponse(
            @JsonProperty("id") Integer id,
            @JsonProperty("jenis_data") String jenisData,
            @JsonProperty("data_kinerja") List<Response> dataKinerja
    ) {}
}