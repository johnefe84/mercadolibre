package com.example.fuegoDeQuasar.controller;

import com.example.fuegoDeQuasar.domain.User;
import com.example.fuegoDeQuasar.dto.InformationDTO;
import com.example.fuegoDeQuasar.dto.MessageDTO;
import com.example.fuegoDeQuasar.dto.PositionDTO;
import com.example.fuegoDeQuasar.dto.SatelliteDTO;
import com.example.fuegoDeQuasar.dto.UserDataDTO;
import com.example.fuegoDeQuasar.enums.Role;
import com.example.fuegoDeQuasar.service.UserService;
import com.example.fuegoDeQuasar.service.impl.InformationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class InformationControllerTest {

    @InjectMocks
    private InformationController controller;

    @Mock
    private InformationServiceImpl service;

    @Mock
    private UserService userService;

    private SatelliteDTO satelliteDTO;
    private String satelliteName;
    private String message;
    private float xPosition;
    private float yPosition;
    private PositionDTO position;
    private MessageDTO messageDTO;

    @Test
    public void getInformationFromSatellitesTest() {
        MockitoAnnotations.initMocks(this);
        initializeObjects();

        InformationDTO informationDTO = new InformationDTO();
        informationDTO.setSatellites(null);

        when(service.decodeMessage(any(InformationDTO.class))).thenReturn(messageDTO);

        ResponseEntity<MessageDTO> successResult = controller.getInformationFromSatellites(informationDTO);

        assertNotNull(Objects.requireNonNull(successResult.getBody()).getMessage());
        assertEquals(position, successResult.getBody().getPosition());
        assertEquals(HttpStatus.OK, successResult.getStatusCode());
        assertEquals(message, successResult.getBody().getMessage());

        verify(service, times(1)).decodeMessage(any(InformationDTO.class));
    }

    @Test
    public void postInformationFromSatelliteTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        initializeObjects();

        when(service.addSatelliteInfo(satelliteDTO, satelliteName)).thenReturn(messageDTO);

        ResponseEntity<MessageDTO> successResult = controller.postInformationFromSatellite(satelliteDTO, satelliteName);

        assertNotNull(Objects.requireNonNull(successResult.getBody()).getMessage());
        assertEquals(position, successResult.getBody().getPosition());
        assertEquals(HttpStatus.OK, successResult.getStatusCode());
        assertEquals(message, successResult.getBody().getMessage());

        verify(service, times(1)).addSatelliteInfo(satelliteDTO, satelliteName);
    }

    @Test
    public void getInformationFromSatelliteTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        initializeObjects();

        when(service.getDecodedInfomation()).thenReturn(messageDTO);

        ResponseEntity<MessageDTO> successResult = controller.getInformationFromSatellite();

        assertNotNull(Objects.requireNonNull(successResult.getBody()).getMessage());
        assertEquals(position, successResult.getBody().getPosition());
        assertEquals(HttpStatus.OK, successResult.getStatusCode());
        assertEquals(message, successResult.getBody().getMessage());

        verify(service, times(1)).getDecodedInfomation();
    }

    @Test
    public void signupTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        initializeObjects();
        String userName = "user";
        String password = "password";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZWxpIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9DTElFTlQifV0sImlhdCI6MTYwMjk5NjYzNCwiZXhwIjoxNjAyOTk2OTM0fQ.Qzs-Wu4ewuSBPI7p_WbHr7RfiwL7ZE2MbVl-paviqYw";
        String email = "user@mercadolibre.com";
        List<Role> roles = new ArrayList<>();
        roles.add(Role.valueOf("ROLE_CLIENT"));

        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setEmail(email);
        userDataDTO.setPassword(password);
        userDataDTO.setUsername(userName);
        userDataDTO.setRoles(roles);

        when(userService.getToken(any(User.class))).thenReturn(token);

        String result = controller.signup(userDataDTO);

        verify(userService, times(1)).getToken(any(User.class));

        assertNotNull(Objects.requireNonNull(result));
        assertEquals(token, result);

    }

    private void initializeObjects(){
        this.satelliteDTO = new SatelliteDTO();
        this.satelliteName = "sato";
        this.message = "este es un mensaje secreto";
        this.xPosition = -487.28592f;
        this.yPosition = 1557.0143f;
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setX(xPosition);
        positionDTO.setY(yPosition);
        this.position = positionDTO;
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setPosition(position);
        messageDTO.setMessage(message);
        this.messageDTO = messageDTO;
    }
}
