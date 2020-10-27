package com.example.fuegoDeQuasar.controller;

import com.example.fuegoDeQuasar.domain.User;
import com.example.fuegoDeQuasar.dto.InformationDTO;
import com.example.fuegoDeQuasar.dto.MessageDTO;
import com.example.fuegoDeQuasar.dto.SatelliteDTO;
import com.example.fuegoDeQuasar.dto.UserDataDTO;
import com.example.fuegoDeQuasar.enums.SatelitteEnum;
import com.example.fuegoDeQuasar.service.InformationService;
import com.example.fuegoDeQuasar.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.modelmapper.ModelMapper;

@Slf4j
@RestController
public class InformationController {

    private final InformationService service;
    private UserService userService;

    public InformationController(InformationService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @ApiOperation(value = "Get information from satellite")
    @PostMapping(value = "topsecret", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> getInformationFromSatellites(@RequestBody InformationDTO information) {
        log.info("Ingresando al metodo getInformationFromSatellites");
        ResponseEntity<MessageDTO> result;

        MessageDTO messageDTO = service.decodeMessage(information);
        result = new ResponseEntity<>(messageDTO, HttpStatus.OK);

        log.info("Saliendo del metodo getInformationFromSatellites");
        return result;
    }

    @ApiOperation(value = "Get information from satellite")
    @PostMapping(value = "topsecret_split/{satellite_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> postInformationFromSatellite(
            @RequestBody SatelliteDTO satInformation,
            @PathVariable String satellite_name) throws Exception {
        log.info("Entering method getInformationFromSatellite");
        ResponseEntity<MessageDTO> result;

        MessageDTO messageDTO = service.addSatelliteInfo(satInformation, SatelitteEnum.valueOf(satellite_name).name());
        result = new ResponseEntity<>(messageDTO, HttpStatus.OK);

        log.info("Saliendo del metodo getInformationFromSatellite");
        return result;
    }

    @GetMapping(value = "topsecret_split", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "${InformationController.topsecret_split}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> getInformationFromSatellite() throws Exception {
        log.info("Ingresando al metodo getInformationFromSatellite");
        ResponseEntity<MessageDTO> result;

        MessageDTO messageSplitDTO = service.getDecodedInfomation();
        result = new ResponseEntity<>(messageSplitDTO, HttpStatus.OK);

        log.info("Saliendo del metodo getInformationFromSatellite");
        return result;
    }

    @PostMapping("/signup")
    @ApiOperation(value = "${UserController.signup}")
    public String signup(@ApiParam("Signup User") @RequestBody UserDataDTO user) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        return userService.getToken(modelMapper.map(user, User.class));
    }
}
