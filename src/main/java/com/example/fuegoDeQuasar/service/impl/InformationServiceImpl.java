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

import com.example.fuegoDeQuasar.util.Mapper;
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

    private final SatelliteRepository satelliteRepository;

    private MessagesRepository messagesRepository;

    public InformationServiceImpl(SatelliteRepository satelliteRepository) {
        this.satelliteRepository = satelliteRepository;
    }

    @Autowired
    public InformationServiceImpl(SatelliteRepository satelliteRepository, MessagesRepository messagesRepository) {
        this.satelliteRepository = satelliteRepository;
        this.messagesRepository = messagesRepository;
    }

    /**
     * Crea el mensaje de respuesta que contiene el mensaje secreto y las coordenadas
     * del emisor una vez se realizaron los calculos.
     *
     * @param information Contiene el request con la informacion y distancia de los 3 satelites
     */
    @Override
    public MessageDTO decodeMessage(InformationDTO information) {
        String message = GetMessage(information);
        PositionDTO position = GetLocation(information);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setPosition(position);
        messageDTO.setMessage(message);

        return messageDTO;
    }

    /**
     * Recibe el request POST y almacena la informacion en una base de datos en memoria H2
     * a fin de procesarla mas adelante cuando se haga el request del GET y si se recibe informacion
     * de un satelite que ya se habia almacenado previamente enntonces se actualiza la informacion
     * de esta manera solamente pueden haber maximo 3 registros en la tabla de satelites y no se afectaran
     * los calculos
     *
     * @param satInformation Contiene la informacion, distancia y mensaje de 1 satelite
     * @param name nombre del satelite del cual se recibio la informacion
     */
    @Override
    public MessageDTO addSatelliteInfo(SatelliteDTO satInformation, String name) {
        try {
            Satellite satelliteSaved;

            satInformation.setNombre(SatelitteEnum.valueOf(name));
            Optional<Satellite> satelite = satelliteRepository.findByNombre(SatelitteEnum.valueOf(name));

            if (satelite.isPresent()) {
                satelite.get().setDistancia(satInformation.getDistancia());
                satelliteSaved = satelliteRepository.save(satelite.get());
            } else {
                satelliteSaved = satelliteRepository.save(Mapper.toEntity(satInformation));
            }
            for (String message : satInformation.getMessage()) {
                Messages msg = new Messages();
                msg.setIdSatellite(satelliteSaved.getId());
                msg.setMessage(message);

                messagesRepository.save(msg);
            }
        } catch (PersistenceException persistenceException) {
            throw new PersistenceException(SAVING_ERROR);
        }

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(SUCCESSFULLY);

        return messageDTO;
    }

    /**
     * Va a la base de datos en memoria H2 y extrae la informacion contenida en las tablas satelite y menssage
     * luego arma una instancia de InformationDTO para luego pasarlo como parametro al metodo decodeMessage el cual
     * tiene la logica para decifrar el mensaje teniendo la informacion de los 3 satelites
     *
     */
    @Override
    public MessageDTO getDecodedInfomation() throws Exception {
        List<Satellite> satellites = satelliteRepository.findAll();
        List<SatelliteDTO> satellitesDTOList = new ArrayList<>();

        if (satellites.isEmpty()) {
            throw new Exception(NO_SAT_INFO);
        }

        if (satellites.size() < AMOUNT_SATS) {
            throw new Exception(NO_ENOUGH_SAT_INFO);
        }

        for (Satellite sat : satellites) {
            List<Messages> messagesList = messagesRepository.findDistinctByIdSatellite(sat.getId());
            SatelliteDTO satelliteDTO = Mapper.toDTO(sat);
            satelliteDTO.setMessage(convertMessagesToArray(messagesList));
            satellitesDTOList.add(satelliteDTO);
        }

        InformationDTO informationDTO = new InformationDTO();
        informationDTO.setSatellites(satellitesDTOList);


        return decodeMessage(informationDTO);
    }

    /**
     * Este metodo transforma el listado en un arreglo de String ya que con dicho formato es necesario
     * que sea enviadocomo parametro al metodo que decodificara el mensaje mas adelante
     *
     * @param messagesList Recibe un listado de Messages recuperados desde la base de datos H2
     */
    private String[] convertMessagesToArray(List<Messages> messagesList) {
        String[] result = new String[messagesList.size()];
        int counter = 0;

        for (Messages msg : messagesList) {
            result[counter] = msg.getMessage();
            counter++;
        }

        return result;
    }

    /**
     * Este metodo realiza el calculo de ubicacion de emisor.
     * La solucion se basa en la forma como funciona un GPS
     * La interseccion de los 3 circulos que forman las 3 distancias
     * que hay entre el objetivo y cada satelite.
     * Para eso se usa la ecuacion del circulo r^2 = x^2 + y^2
     * donde r es el radio x,y son las coordenadas del centro del circulo
     *
     * @param information Contiene el request con la informacion y distancia de los 3 satelites
     */
    private PositionDTO GetLocation(InformationDTO information) {
        float r1;
        float r2;
        float r3;
        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;

        double enemyPositionX;
        double enemyPositionY;

        List<SatelliteDTO> satellites = information.getSatellites();

        SatellitePositionDTO satellitePositionDTO1 = new SatellitePositionDTO();
        satellitePositionDTO1.setSatellite(satellites.get(0));
        satellitePositionDTO1.setPosition(setPosition(satellites.get(0).getNombre().name()));

        SatellitePositionDTO satellitePositionDTO2 = new SatellitePositionDTO();
        satellitePositionDTO2.setSatellite(satellites.get(1));
        satellitePositionDTO2.setPosition(setPosition(satellites.get(1).getNombre().name()));

        SatellitePositionDTO satellitePositionDTO3 = new SatellitePositionDTO();
        satellitePositionDTO3.setSatellite(satellites.get(2));
        satellitePositionDTO3.setPosition(setPosition(satellites.get(2).getNombre().name()));

        r1 = satellites.get(0).getDistancia();
        r2 = satellites.get(1).getDistancia();
        r3 = satellites.get(2).getDistancia();

        x1 = satellitePositionDTO1.getPosition().getX();
        y1 = satellitePositionDTO1.getPosition().getY();

        x2 = satellitePositionDTO2.getPosition().getX();
        y2 = satellitePositionDTO2.getPosition().getY();

        x3 = satellitePositionDTO3.getPosition().getX();
        y3 = satellitePositionDTO3.getPosition().getY();

        float A = 2 * x2 - 2 * x1;
        float B = 2 * y2 - 2 * y1;
        double C = pow(r1, 2) - pow(r2, 2) - pow(x1, 2) + pow(x2, 2) - pow(y1, 2) + pow(y2, 2);
        float D = 2 * x3 - 2 * x2;
        float E = 2 * y3 - 2 * y2;
        double F = pow(r2, 2) - pow(r3, 2) - pow(x2, 2) + pow(x3, 2) - pow(y2, 2) + pow(y3, 2);
        enemyPositionX = (C * E - F * B) / (E * A - B * D);
        enemyPositionY = (C * D - A * F) / (B * D - A * E);

        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setX((float) enemyPositionX);
        positionDTO.setY((float) enemyPositionY);

        return positionDTO;
    }

    /**
     * Este metodo retorna las coordenadas conocidas de cada satelite
     * para ello recibe el nombre y retorna la posicion
     *
     * @param satName Nombre del satelite
     */
    private PositionDTO setPosition(String satName) {
        PositionDTO position = new PositionDTO();

        switch (satName) {
            case "kenobi":
                position.setX(-500f);
                position.setY(-200f);
                break;
            case "skywalker":
                position.setX(100f);
                position.setY(-100f);
                break;
            case "sato":
                position.setX(500f);
                position.setY(100f);
                break;
        }
        return position;
    }

    /**
     * Este metodo determina el mensaje secreto basado en la superposicion de los mensajes
     * para asi lograr un mensaje completo basado en la informacion recibida por los 3 satelites
     *
     * @param information Contiene el request con la informacion y distancia de los 3 satelites
     */
    private String GetMessage(InformationDTO information) {
        String[] messages = new String[5];

        for (SatelliteDTO satelliteDTO : information.getSatellites()) {
            for (int position = 0; position < 5; position++) {
                messages[position] = StringUtils.isEmpty(messages[position]) ? satelliteDTO.getMessage()[position] : messages[position];
            }
        }

        return String.join(" ", messages);
    }
}
