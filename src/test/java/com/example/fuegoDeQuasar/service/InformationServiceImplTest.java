package com.example.fuegoDeQuasar.service;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.transaction.Transactional;

@RunWith(MockitoJUnitRunner.Silent.class)
//@ContextConfiguration(locations = {"classpath:db-context.xml"})
@Transactional
public class InformationServiceImplTest {

    /*@InjectMocks
    private InformationServiceImpl informationServiceImpl;

    @Autowired
    private SatelliteRepository satelliteRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test(){
        informationServiceImpl = new InformationServiceImpl(satelliteRepository, messagesRepository);
        String satName = "sato";
        String[] message = {"este","","","mensaje",""};

        SatelliteDTO satelliteDTO = SatelliteDTO
                .builder()
                .distancia(100)
                .nombre(SatelitteEnum.valueOf(satName))
                .message(message)
                .build();

        when(satelliteRepository.findByNombre(SatelitteEnum.valueOf(satName)))
                .thenReturn(Optional.of(Mapper.toEntity(satelliteDTO)));

        MessageDTO messageDTO = informationServiceImpl.addSatelliteInfo(satelliteDTO, satName);
    }*/
}
