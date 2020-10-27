package com.example.fuegoDeQuasar.domain;

import com.example.fuegoDeQuasar.dto.PositionDTO;
import com.example.fuegoDeQuasar.dto.SatelliteDTO;
import com.example.fuegoDeQuasar.dto.SatellitePositionDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SatellitePositionDTOTest {

    @Test
    public void satellitePositionDTO() {
        SatelliteDTO satellite = new SatelliteDTO();
        PositionDTO position = new PositionDTO();
        SatellitePositionDTO satellitePositionDTO1 = new SatellitePositionDTO();
        satellitePositionDTO1.setSatellite(satellite);
        satellitePositionDTO1.setPosition(position);

        assertEquals(satellitePositionDTO1.getPosition(), position);
        assertEquals(satellitePositionDTO1.getSatellite(), satellite);
    }
}
