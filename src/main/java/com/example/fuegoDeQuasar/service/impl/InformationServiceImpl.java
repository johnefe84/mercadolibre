package com.example.fuegoDeQuasar.service.impl;

import com.example.fuegoDeQuasar.domain.Messages;
import com.example.fuegoDeQuasar.domain.Satellite;
import com.example.fuegoDeQuasar.dto.InformationDTO;
import com.example.fuegoDeQuasar.dto.MessageDTO;
import com.example.fuegoDeQuasar.dto.PositionDTO;
import com.example.fuegoDeQuasar.dto.SatelliteDTO;
import com.example.fuegoDeQuasar.dto.SatellitePositionDTO;
import com.example.fuegoDeQuasar.enums.SatelitteEnum;
import com.example.fuegoDeQuasar.repository.MessagesRepository;
import com.example.fuegoDeQuasar.repository.SatelliteRepository;
import com.example.fuegoDeQuasar.service.InformationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.PersistenceException;

import static com.example.fuegoDeQuasar.util.Constants.AMOUNT_SATS;
import static com.example.fuegoDeQuasar.util.Constants.NO_ENOUGH_SAT_INFO;
import static com.example.fuegoDeQuasar.util.Constants.NO_SAT_INFO;
import static com.example.fuegoDeQuasar.util.Constants.SAVING_ERROR;
import static com.example.fuegoDeQuasar.util.Constants.SUCCESSFULLY;
import static java.lang.StrictMath.pow;

@Component
public class InformationServiceImpl implements InformationService {

    @Autowired
    private SatelliteRepository satelliteRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    @Override
    public MessageDTO decodeMessage(InformationDTO information) {
        String message = GetMessage(information);
        PositionDTO position = GetLocation(information);

        MessageDTO messageDTO = MessageDTO
                .builder()
                .position(position)
                .message(message)
                .build();
        return messageDTO;
    }

    @Override
    public MessageDTO decodeMessageSat(SatelliteDTO satInformation) {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .build();
        return messageDTO;
    }

    @Override
    public MessageDTO addSatelliteInfo(SatelliteDTO satInformation, String name) {
        try {
            Satellite satelliteSaved;

            satInformation.setNombre(SatelitteEnum.valueOf(name));
            Optional<Satellite> satelite = satelliteRepository.findByNombre(SatelitteEnum.valueOf(name));

            if(satelite.isPresent()){
                satelite.get().setDistancia(satInformation.getDistancia());
                satelliteSaved = satelliteRepository.save(satelite.get());
            }
            else{
                satelliteSaved = satelliteRepository.save(toEntity(satInformation));
            }
            for(String message: satInformation.getMessage()) {
                //Optional<Messages> messageFound = messagesRepository
                //        .findByIdSatelliteAndMessage(satelliteSaved.getId(),message);

                //if(!messageFound.isPresent()) {
                    Messages msg = Messages
                            .builder()
                            .idSatellite(satelliteSaved.getId())
                            .message(message)
                            .build();

                    messagesRepository.save(msg);
                //}
            }
        }
        catch(PersistenceException persistenceException){
            throw new PersistenceException(SAVING_ERROR);
        }
        return MessageDTO.builder().message(SUCCESSFULLY).build();
    }

    @Override
    public MessageDTO getDecodedInfomation() throws Exception {
        List<Satellite> satellites = satelliteRepository.findAll();
        List<SatelliteDTO> satellitesDTOList = new ArrayList<>();

        if(satellites.isEmpty()){
            throw new Exception(NO_SAT_INFO);
        }

        if(satellites.size() < AMOUNT_SATS){
            throw new Exception(NO_ENOUGH_SAT_INFO);
        }

        for(Satellite sat: satellites){
            List<Messages> messagesList = messagesRepository.findDistinctByIdSatellite(sat.getId());
            SatelliteDTO satelliteDTO = toDTO(sat);
            satelliteDTO.setMessage(convertMessagesToArray(messagesList));
            satellitesDTOList.add(satelliteDTO);
        }

        InformationDTO informationDTO = InformationDTO
                .builder()
                .satellites(satellitesDTOList)
                .build();

        MessageDTO messageDTO = decodeMessage(informationDTO);

        return messageDTO;
    }

    private String[] convertMessagesToArray(List<Messages> messagesList) {
        String[] result = new String[messagesList.size()];
        int counter = 0;

        for(Messages msg :messagesList){
            result[counter] = msg.getMessage();
            counter++;
        }

        return result;
    }


    // input: distancia al emisor tal cual se recibe en cada satélite
    // output: las coordenadas ‘x’ e ‘y’ del emisor del mensaje
    private PositionDTO GetLocation(InformationDTO information){
        //La solucion se basa en la forma como funciona un GPS
        //La interseccion de los 3 circulos que forman las 3 distancias
        //que hay entre el objetivo y cada satelite.
        //Para eso se usa la ecuacion del circulo r^2 = x^2 + y^2
        //donde r es el radio x,y son las coordenadas del centro del circulo
        float r1;
        float r2;
        float r3;
        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;

        Double enemyPositionX;
        Double enemyPositionY;

        List<SatelliteDTO> satellites = information.getSatellites();

        SatellitePositionDTO satellitePositionDTO1 = SatellitePositionDTO
                .builder()
                .satellite(satellites.get(0))
                .position(setPosition(satellites.get(0).getNombre().name()))
                .build();

        SatellitePositionDTO satellitePositionDTO2 = SatellitePositionDTO
                .builder()
                .satellite(satellites.get(1))
                .position(setPosition(satellites.get(1).getNombre().name()))
                .build();

        SatellitePositionDTO satellitePositionDTO3 = SatellitePositionDTO
                .builder()
                .satellite(satellites.get(2))
                .position(setPosition(satellites.get(2).getNombre().name()))
                .build();

        r1 = satellites.get(0).getDistancia();
        r2 = satellites.get(1).getDistancia();
        r3 = satellites.get(2).getDistancia();

        x1 = satellitePositionDTO1.getPosition().getX();
        y1 = satellitePositionDTO1.getPosition().getY();

        x2 = satellitePositionDTO2.getPosition().getX();
        y2 = satellitePositionDTO2.getPosition().getY();

        x3 = satellitePositionDTO3.getPosition().getX();
        y3 = satellitePositionDTO3.getPosition().getY();

        float A = 2*x2 - 2*x1;
        float B = 2*y2 - 2*y1;
        Double C = pow(r1,2) - pow(r2,2) - pow(x1,2) + pow(x2,2) - pow(y1,2) + pow(y2,2);
        float D = 2*x3 - 2*x2;
        float E = 2*y3 - 2*y2;
        Double F = pow(r2,2) - pow(r3,2) - pow(x2,2) + pow(x3,2) - pow(y2,2) + pow(y3,2);
        enemyPositionX = (C*E - F*B) / (E*A - B*D);
        enemyPositionY = (C*D - A*F) / (B*D - A*E);

        PositionDTO positionDTO = PositionDTO
                .builder()
                .x(enemyPositionX.floatValue())
                .y(enemyPositionY.floatValue())
                .build();

        return positionDTO;
    }

    private PositionDTO setPosition(String satName){
        PositionDTO position = null;

        switch(satName){
            case "kenobi":
                position = PositionDTO
                        .builder()
                        .x(-500)
                        .y(-200)
                        .build();
                break;
            case "skywalker":
                position = PositionDTO
                        .builder()
                        .x(100)
                        .y(-100)
                        .build();
                break;
            case "sato":
                position = PositionDTO
                        .builder()
                        .x(500)
                        .y(100)
                        .build();
                break;
        }
        return position;
    }

    // input: el mensaje tal cual es recibido en cada satélite
    // output: el mensaje tal cual lo genera el emisor del mensaje
    private String GetMessage(InformationDTO information){
        String[] messages = new String[information.getSatellites().get(0).getMessage().length];

        for(SatelliteDTO satelliteDTO : information.getSatellites()){
            for(int position = 0; position < satelliteDTO.getMessage().length; position++) {
                messages[position] = StringUtils.isEmpty(messages[position]) ? satelliteDTO.getMessage()[position] : messages[position];
            }
        }

        return String.join(" ",messages);
    }

    private Satellite toEntity(SatelliteDTO satelliteDTO){
        Satellite satellite = Satellite
                .builder()
                .distancia(satelliteDTO.getDistancia())
                .nombre(satelliteDTO.getNombre())
                .build();

        return satellite;
    }

    private SatelliteDTO toDTO(Satellite satellite){
        SatelliteDTO satelliteDTO = SatelliteDTO
                .builder()
                .distancia(satellite.getDistancia())
                .nombre(satellite.getNombre())
                .build();

        return satelliteDTO;
    }
}
