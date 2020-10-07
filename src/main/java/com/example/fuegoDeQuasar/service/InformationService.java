package com.example.fuegoDeQuasar.service;

import com.example.fuegoDeQuasar.dto.InformationDTO;
import com.example.fuegoDeQuasar.dto.MessageDTO;
import com.example.fuegoDeQuasar.dto.SatelliteDTO;

public interface InformationService {
    MessageDTO decodeMessage(InformationDTO information);

    MessageDTO addSatelliteInfo(SatelliteDTO satInformation, String name);

    MessageDTO getDecodedInfomation() throws Exception;
}
