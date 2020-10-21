package com.example.fuegoDeQuasar.util;

import com.example.fuegoDeQuasar.domain.Satellite;
import com.example.fuegoDeQuasar.dto.SatelliteDTO;

public class Mapper {
    /**
     * Este metodo mapea desde un DTO hacia un Entity para posteriormente ser alomacenado en
     * la base de datos en memoria H2
     *
     * @param satelliteDTO el DTO con la infomracion de 1 satelite
     */
    public static Satellite toEntity(SatelliteDTO satelliteDTO) {
        Satellite satellite = new Satellite();
        satellite.setDistancia(satelliteDTO.getDistancia());
        satellite.setNombre(satelliteDTO.getNombre());

        return satellite;
    }

    /**
     * Este metodo mapea desde un Entity recuperado desde la base de datos H2
     * hacia un DTO para poder ser seteado mas adelante a un objeto InformationDTO y asi ser procesado
     * por el metodo decodeMessage
     *
     * @param satellite el entity devuelto desde la base de datos referente a 1 satelite
     */
    public static SatelliteDTO toDTO(Satellite satellite) {

        SatelliteDTO satelliteDTO = new SatelliteDTO();
        satelliteDTO.setDistancia(satellite.getDistancia());
        satelliteDTO.setNombre(satellite.getNombre());

        return satelliteDTO;
    }
}
