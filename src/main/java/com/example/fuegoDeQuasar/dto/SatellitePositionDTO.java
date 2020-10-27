package com.example.fuegoDeQuasar.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SatellitePositionDTO {
    private SatelliteDTO satellite;
    private PositionDTO position;
}
