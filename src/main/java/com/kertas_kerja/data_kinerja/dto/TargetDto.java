package com.kertas_kerja.data_kinerja.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class TargetDto {

    @Builder
    public record CreateRequest(
            @NotBlank @JsonProperty("target") String target,
            @NotBlank @JsonProperty("satuan") String satuan,
            @NotBlank @JsonProperty("tahun") String tahun
    ) {}

    @Builder
    public record UpdateRequest(
            @JsonProperty("id") Integer id,
            @NotBlank @JsonProperty("target") String target,
            @NotBlank @JsonProperty("satuan") String satuan,
            @NotBlank @JsonProperty("tahun") String tahun
    ) {}

    @Builder
    public record Response(
            @JsonProperty("id") Integer id,
            @JsonProperty("target") String target,
            @JsonProperty("satuan") String satuan,
            @JsonProperty("tahun") String tahun
    ) {}
}