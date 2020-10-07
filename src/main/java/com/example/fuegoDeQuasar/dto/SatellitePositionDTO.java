package com.example.fuegoDeQuasar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SatellitePositionDTO {
    private SatelliteDTO satellite;
    private PositionDTO position;
}
