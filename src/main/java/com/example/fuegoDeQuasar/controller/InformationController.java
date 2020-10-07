package com.example.fuegoDeQuasar.controller;

import com.example.fuegoDeQuasar.dto.InformationDTO;
import com.example.fuegoDeQuasar.dto.MessageDTO;
import com.example.fuegoDeQuasar.dto.SatelliteDTO;
import com.example.fuegoDeQuasar.enums.SatelitteEnum;
import com.example.fuegoDeQuasar.service.InformationService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class InformationController {

    private final InformationService service;

    public InformationController(InformationService service) {
        this.service = service;
    }

    @ApiOperation(value = "Get information from satellite")
    @PostMapping(value = "topsecret", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> getInformationFromSatellites(@RequestBody InformationDTO information) {
        log.info("Entering method getInformationFromSatellites");
        ResponseEntity<MessageDTO> result;

        MessageDTO messageDTO = service.decodeMessage(information);
        result = new ResponseEntity<>(messageDTO, HttpStatus.OK);

        log.info("Exiting method getInformationFromSatellites");
        return result;
    }

    @ApiOperation(value = "Get information from satellite")
    @PostMapping(value = "topsecret_split/{satellite_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> postInformationFromSatellite(@RequestBody SatelliteDTO satInformation,
                                                                   @PathVariable String satellite_name) {
        log.info("Entering method getInformationFromSatellite");
        ResponseEntity<MessageDTO> result;

        MessageDTO messageDTO = service.addSatelliteInfo(satInformation, SatelitteEnum.valueOf(satellite_name).name());
        result = new ResponseEntity<>(messageDTO, HttpStatus.OK);

        log.info("Exiting method getInformationFromSatellite");
        return result;
    }

    @ApiOperation(value = "Get information from satellite")
    @GetMapping(value = "topsecret_split", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> getInformationFromSatellite() throws Exception {
        log.info("Entering method getInformationFromSatellite");
        ResponseEntity<MessageDTO> result;

        MessageDTO messageSplitDTO = service.getDecodedInfomation();
        result = new ResponseEntity<>(messageSplitDTO, HttpStatus.OK);

        log.info("Exiting method getInformationFromSatellite");
        return result;
    }
}
