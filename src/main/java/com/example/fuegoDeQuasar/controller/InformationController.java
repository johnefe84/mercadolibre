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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public InformationController(InformationService service) {
        this.service = service;
    }

    @Autowired
    private UserService userService;

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
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use")})
    public ResponseEntity<MessageDTO> postInformationFromSatellite(
            @RequestBody SatelliteDTO satInformation,
            @PathVariable String satellite_name) throws Exception {
        log.info("Entering method getInformationFromSatellite");
        ResponseEntity<MessageDTO> result;

        MessageDTO messageDTO = service.addSatelliteInfo(satInformation, SatelitteEnum.valueOf(satellite_name).name());
        result = new ResponseEntity<>(messageDTO, HttpStatus.OK);

        log.info("Exiting method getInformationFromSatellite");
        return result;
    }

    //@ApiOperation(value = "Get information from satellite")
    @GetMapping(value = "topsecret_split", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "${InformationController.topsecret_split}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<MessageDTO> getInformationFromSatellite() throws Exception {
        log.info("Entering method getInformationFromSatellite");
        ResponseEntity<MessageDTO> result;

        MessageDTO messageSplitDTO = service.getDecodedInfomation();
        result = new ResponseEntity<>(messageSplitDTO, HttpStatus.OK);

        log.info("Exiting method getInformationFromSatellite");
        return result;
    }

    @PostMapping("/signup")
    @ApiOperation(value = "${UserController.signup}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use")})
    public String signup(@ApiParam("Signup User") @RequestBody UserDataDTO user) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        return userService.getToken(modelMapper.map(user, User.class));
    }
}
