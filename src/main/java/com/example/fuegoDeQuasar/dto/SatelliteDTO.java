package com.example.fuegoDeQuasar.dto;

import com.example.fuegoDeQuasar.enums.SatelitteEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SatelliteDTO {
    private SatelitteEnum nombre;
    private float distancia;
    private String[] message;
}
