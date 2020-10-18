package com.example.fuegoDeQuasar.controller;

import com.example.fuegoDeQuasar.dto.InformationDTO;
import com.example.fuegoDeQuasar.dto.MessageDTO;
import com.example.fuegoDeQuasar.dto.PositionDTO;
import com.example.fuegoDeQuasar.service.InformationService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class InformationControllerTest {

    @InjectMocks
    private InformationController controller;

    @Mock
    private InformationService service;

    @Test
    public void getInformationFromSatellitesTest() {
        MockitoAnnotations.initMocks(this);
        final float xPosition = -487.28592f;
        final float yPosition = 1557.0143f;

        PositionDTO position = PositionDTO.builder().x(xPosition).y(yPosition).build();
        MessageDTO messageDTO = MessageDTO.builder().position(position).message("este es un mensaje secreto").build();

        when(service.decodeMessage(any(InformationDTO.class))).thenReturn(messageDTO);

        ResponseEntity<MessageDTO> successResult = controller.getInformationFromSatellites(InformationDTO.builder().build());

        assertNotNull(Objects.requireNonNull(successResult.getBody()).getMessage());
        assertEquals(position, successResult.getBody().getPosition());
        assertEquals(HttpStatus.OK, successResult.getStatusCode());

        verify(service).decodeMessage(any(InformationDTO.class));
    }

    @Test
    public void postInformationFromSatelliteTest(){

    }

    @Test
    public void getInformationFromSatelliteTest(){

    }

    @Test
    public void signupTest(){

    }
}
