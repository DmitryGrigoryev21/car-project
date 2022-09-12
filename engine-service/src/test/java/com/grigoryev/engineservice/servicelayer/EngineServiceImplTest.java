package com.grigoryev.engineservice.servicelayer;

import com.grigoryev.engineservice.deliverancelayer.Engine;
import com.grigoryev.engineservice.deliverancelayer.EngineRepository;
import com.grigoryev.engineservice.util.EntityDTOUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 27017"})
@AutoConfigureWebTestClient
class EngineServiceImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EngineService engineService;

    @MockBean
    private EngineRepository repo;

    @Test
    void getAll() {
    }

    @Test
    void insertEngine() {

        Engine engineEntity = buildEngine();
        EntityDTOUtil dtoObj = new EntityDTOUtil();


        engineService.insertEngine(Mono.just(dtoObj.toDTO(engineEntity)));

        assertNull(repo.findEngineByEngineUUID(engineEntity.getEngineUUID()));      // Not working for some reason
    }

    @Test
    void getEngineByEngineUUID() {


        Engine engineEntity = buildEngine();

        String ENGINE_UUID = engineEntity.getEngineUUID();

        when(repo.findEngineByEngineUUID(anyString())).thenReturn(Mono.just(engineEntity));

        Mono<EngineDTO> engineDTOMono = engineService.getEngineByEngineUUID(ENGINE_UUID);

        StepVerifier.create(engineDTOMono)      // What makes this more reactive than the previous stuff we have completed in WebServices?
                .consumeNextWith(foundEngine -> {
                    assertEquals(engineEntity.getEngineUUID(), foundEngine.getEngineUUID());
                    assertEquals(engineEntity.getCarUUID(), foundEngine.getCarUUID());
                    assertEquals(engineEntity.getFuelType(), foundEngine.getFuelType());
                })
                .verifyComplete();
    }

    @Test
    void getEnginesByCarUUID() {

        Engine engineEntity = buildEngine();

        String ENGINE_UUID = engineEntity.getEngineUUID();

        when(repo.findEngineByEngineUUID(anyString())).thenReturn(Mono.just(engineEntity));

        Mono<EngineDTO> engineDTOMono = engineService.getEngineByEngineUUID(ENGINE_UUID);

        StepVerifier.create(engineDTOMono)      // What makes this more reactive than the previous stuff we have completed in WebServices?
                .consumeNextWith(foundEngine -> {
                    assertEquals(engineEntity.getEngineUUID(), foundEngine.getEngineUUID());
                    assertEquals(engineEntity.getCarUUID(), foundEngine.getCarUUID());
                    assertEquals(engineEntity.getFuelType(), foundEngine.getFuelType());
                })
                .verifyComplete();
    }

    @Test
    void updateEngine() {
        Engine engineEntity = buildEngine();
        Engine updatedEngine = Engine.builder().id("Id").engineUUID("EngineUUID")
                .carUUID("CarUUID").name("LB7").cylinders(8).fuelType("Diesel").price(6700).build();

        EntityDTOUtil dtoObj = new EntityDTOUtil();

        when(repo.findEngineByEngineUUID(anyString())).thenReturn(Mono.just(engineEntity));

        engineService.updateEngine(engineEntity.getEngineUUID(), Mono.just(dtoObj.toDTO(updatedEngine)));

        assertNotEquals(Mono.just(engineEntity), repo.findEngineByEngineUUID(updatedEngine.getEngineUUID()));

    }

    @Test
    void deleteEngineByEngineUUID() {
        Engine engineEntity = buildEngine();
        engineService.deleteEngine(engineEntity.getEngineUUID());
    }

    @Test
    void deleteEngineByCarUUID() {

        Engine engineEntity = buildEngine();

        engineService.deleteEngineByCar(engineEntity.getCarUUID());

        assertNull(repo.findEnginesByCarUUID(engineEntity.getCarUUID()));

    }

    private Engine buildEngine(){
        return Engine.builder().id("Id").engineUUID("EngineUUID")
                .carUUID("CarUUID").name("L5P").cylinders(8).fuelType("Diesel").price(10000).build();
    }
}