package com.example.fuegoDeQuasar.service;

import com.example.fuegoDeQuasar.domain.Messages;
import com.example.fuegoDeQuasar.domain.Satellite;
import com.example.fuegoDeQuasar.dto.MessageDTO;
import com.example.fuegoDeQuasar.dto.SatelliteDTO;
import com.example.fuegoDeQuasar.enums.SatelitteEnum;
import com.example.fuegoDeQuasar.repository.MessagesRepository;
import com.example.fuegoDeQuasar.repository.SatelliteRepository;
import com.example.fuegoDeQuasar.service.impl.InformationServiceImpl;
import com.example.fuegoDeQuasar.util.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.PersistenceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class InformationServiceImplTest {

    @InjectMocks
    private InformationServiceImpl informationServiceImpl;

    @Mock
    private SatelliteRepository satelliteRepository;

    @Mock
    private MessagesRepository messagesRepository;

    @Test
    public void addSatelliteInfoTest(){
        MockitoAnnotations.initMocks(this);
        informationServiceImpl = new InformationServiceImpl(satelliteRepository, messagesRepository);
        String satName = "sato";
        String[] message = {"este","","","mensaje",""};

        SatelliteDTO satelliteDTO = new SatelliteDTO();
        satelliteDTO.setDistancia(100);
        satelliteDTO.setNombre(SatelitteEnum.valueOf(satName));
        satelliteDTO.setMessage(message);

        Satellite sat = Mapper.toEntity(satelliteDTO);
        sat.setId(1);

        when(satelliteRepository.save(any(Satellite.class))).thenReturn(sat);

        when(satelliteRepository.findByNombre(SatelitteEnum.valueOf(satName)))
                .thenReturn(Optional.of(Mapper.toEntity(satelliteDTO)));

        MessageDTO messageDTO = informationServiceImpl.addSatelliteInfo(satelliteDTO, satName);

        verify(satelliteRepository, times(1)).save(any(Satellite.class));
        verify(satelliteRepository, times(1)).findByNombre(SatelitteEnum.valueOf(satName));

        assertNotNull(Objects.requireNonNull(messageDTO));
        assertEquals("Informacion del satelite almacenada con exito", messageDTO.getMessage());
    }

    @Test
    public void addSatelliteInfoSatelliteNotPresentTest(){
        MockitoAnnotations.initMocks(this);
        informationServiceImpl = new InformationServiceImpl(satelliteRepository, messagesRepository);
        String satName = "sato";
        String[] message = {"este","","","mensaje",""};

        SatelliteDTO satelliteDTO = new SatelliteDTO();
        satelliteDTO.setDistancia(100);
        satelliteDTO.setNombre(SatelitteEnum.valueOf(satName));
        satelliteDTO.setMessage(message);

        Satellite sat = Mapper.toEntity(satelliteDTO);
        sat.setId(1);

        when(satelliteRepository.save(any(Satellite.class))).thenReturn(sat);

        when(satelliteRepository.findByNombre(SatelitteEnum.valueOf(satName)))
                .thenReturn(Optional.empty());

        MessageDTO messageDTO = informationServiceImpl.addSatelliteInfo(satelliteDTO, satName);

        verify(satelliteRepository, times(1)).save(any(Satellite.class));
        verify(satelliteRepository, times(1)).findByNombre(SatelitteEnum.valueOf(satName));

        assertNotNull(Objects.requireNonNull(messageDTO));
        assertEquals("Informacion del satelite almacenada con exito", messageDTO.getMessage());
    }

    @Test
    public void addSatelliteInfoPersistenceExceptionTest(){
        MockitoAnnotations.initMocks(this);
        informationServiceImpl = new InformationServiceImpl(satelliteRepository, messagesRepository);
        String satName = "sato";
        String[] message = {"este","","","mensaje",""};

        SatelliteDTO satelliteDTO = new SatelliteDTO();
        satelliteDTO.setDistancia(100);
        satelliteDTO.setNombre(SatelitteEnum.valueOf(satName));
        satelliteDTO.setMessage(message);

        Satellite sat = Mapper.toEntity(satelliteDTO);
        sat.setId(1);

        when(satelliteRepository.findByNombre(SatelitteEnum.valueOf(satName))).thenThrow(PersistenceException.class);

        assertThrows(PersistenceException.class,()->{ informationServiceImpl.addSatelliteInfo(satelliteDTO, satName);} );
    }

    @Test
    public void getDecodedInfomationEmptySatellitesTest() {
        MockitoAnnotations.initMocks(this);
        List<Satellite> satellites = new ArrayList<>();
        when(satelliteRepository.findAll()).thenReturn(satellites);

        assertThrows(Exception.class,()->{ informationServiceImpl.getDecodedInfomation();} );
    }

    @Test
    public void getDecodedInformationAll3SatellitesSetTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        final double DELTA = 1e-6;
        List<Satellite> satellites = new ArrayList<>();
        List<Messages> message1List = new ArrayList<>();
        List<Messages> message2List = new ArrayList<>();
        List<Messages> message3List = new ArrayList<>();

        message1List.add(buildMessage(1,"este"));
        message1List.add(buildMessage(1,""));
        message1List.add(buildMessage(1,""));
        message1List.add(buildMessage(1,"mensaje"));
        message1List.add(buildMessage(1,""));

        message2List.add(buildMessage(2,""));
        message2List.add(buildMessage(2,"es"));
        message2List.add(buildMessage(2,""));
        message2List.add(buildMessage(2,""));
        message2List.add(buildMessage(2,"secreto"));

        message3List.add(buildMessage(3,"este"));
        message3List.add(buildMessage(3,""));
        message3List.add(buildMessage(3,"un"));
        message3List.add(buildMessage(3,""));
        message3List.add(buildMessage(3,""));

        Satellite satellite1 = new Satellite();
        satellite1.setId(1);
        satellite1.setNombre(SatelitteEnum.valueOf("kenobi"));
        satellite1.setDistancia(100.0f);
        satellite1.setMessagesList(message1List);

        Satellite satellite2 = new Satellite();
        satellite2.setId(2);
        satellite2.setNombre(SatelitteEnum.valueOf("skywalker"));
        satellite2.setDistancia(115.5f);
        satellite2.setMessagesList(message1List);

        Satellite satellite3 = new Satellite();
        satellite3.setId(3);
        satellite3.setNombre(SatelitteEnum.valueOf("sato"));
        satellite3.setDistancia(142.7f);
        satellite3.setMessagesList(message1List);

        satellites.add(satellite1);
        satellites.add(satellite2);
        satellites.add(satellite3);
        when(satelliteRepository.findAll()).thenReturn(satellites);
        when(messagesRepository.findDistinctByIdSatellite(satellites.get(0).getId())).thenReturn(message1List);
        when(messagesRepository.findDistinctByIdSatellite(satellites.get(1).getId())).thenReturn(message2List);
        when(messagesRepository.findDistinctByIdSatellite(satellites.get(2).getId())).thenReturn(message3List);

        MessageDTO messageDTO = informationServiceImpl.getDecodedInfomation();

        assertNotNull(Objects.requireNonNull(messageDTO));
        assertEquals("este es un mensaje secreto", messageDTO.getMessage());
        assertEquals(-487.28592, messageDTO.getPosition().getX(), DELTA);
        assertEquals(1557.0142822265625, messageDTO.getPosition().getY(), DELTA);

        verify(satelliteRepository, times(1)).findAll();
        verify(messagesRepository, times(3)).findDistinctByIdSatellite(anyInt());
    }

    @Test
    public void getDecodedInformationNot3SatellitesSetTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        List<Satellite> satellites = new ArrayList<>();
        List<Messages> message1List = new ArrayList<>();
        List<Messages> message2List = new ArrayList<>();

        message1List.add(buildMessage(1,"este"));
        message1List.add(buildMessage(1,""));
        message1List.add(buildMessage(1,""));
        message1List.add(buildMessage(1,"mensaje"));
        message1List.add(buildMessage(1,""));

        message2List.add(buildMessage(2,""));
        message2List.add(buildMessage(2,"es"));
        message2List.add(buildMessage(2,""));
        message2List.add(buildMessage(2,""));
        message2List.add(buildMessage(2,"secreto"));

        Satellite satellite1 = new Satellite();
        satellite1.setId(1);
        satellite1.setNombre(SatelitteEnum.valueOf("kenobi"));
        satellite1.setDistancia(100.0f);
        satellite1.setMessagesList(message1List);

        Satellite satellite2 = new Satellite();
        satellite2.setId(2);
        satellite2.setNombre(SatelitteEnum.valueOf("skywalker"));
        satellite2.setDistancia(115.5f);
        satellite2.setMessagesList(message1List);

        satellites.add(satellite1);
        satellites.add(satellite2);

        when(satelliteRepository.findAll()).thenReturn(satellites);
        when(messagesRepository.findDistinctByIdSatellite(satellites.get(0).getId())).thenReturn(message1List);
        when(messagesRepository.findDistinctByIdSatellite(satellites.get(1).getId())).thenReturn(message2List);

        assertThrows(Exception.class,()->{ informationServiceImpl.getDecodedInfomation();} );
    }

    private Messages buildMessage(Integer sat, String message){
        Messages messages = new Messages();
        messages.setIdSatellite(sat);
        messages.setMessage(message);

        return messages;
    }
}
