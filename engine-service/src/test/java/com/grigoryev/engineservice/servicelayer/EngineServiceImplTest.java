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
import reactor.core.publisher.Flux;
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

        // Do not know
    }

    @Test
    void insertEngine() {

        Engine engineEntity = buildEngine();

        String ENGINE_UUID = engineEntity.getEngineUUID();

        when(repo.findEngineByEngineUUID(anyString())).thenReturn(Mono.just(engineEntity));

        Mono<EngineDTO> engineDTOMono = engineService.getEngineByEngineUUID(ENGINE_UUID);

        StepVerifier.create(engineDTOMono)
                .consumeNextWith(foundEngine -> {
                    assertEquals(engineEntity.getEngineUUID(), foundEngine.getEngineUUID());
                    assertEquals(engineEntity.getCarUUID(), foundEngine.getCarUUID());
                    assertEquals(engineEntity.getFuelType(), foundEngine.getFuelType());
                })
                .verifyComplete();
    }

    @Test
    void getEngineByEngineUUID() {


        Engine engineEntity = buildEngine();

        String ENGINE_UUID = engineEntity.getEngineUUID();

        when(repo.findEngineByEngineUUID(anyString())).thenReturn(Mono.just(engineEntity));

        Mono<EngineDTO> engineDTOMono = engineService.getEngineByEngineUUID(ENGINE_UUID);

        StepVerifier.create(engineDTOMono)
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

        String CAR_UUID = engineEntity.getCarUUID();

        when(repo.findEnginesByCarUUID(anyString())).thenReturn(Flux.just(engineEntity));

        Flux<EngineDTO> engineDTOMono = engineService.getEnginesByCarUUID(CAR_UUID);

        StepVerifier.create(engineDTOMono)
                .consumeNextWith(foundEngine -> {
                    assertEquals(engineEntity.getEngineUUID(), foundEngine.getEngineUUID());
                    assertEquals(engineEntity.getCarUUID(), foundEngine.getCarUUID());
                    assertEquals(engineEntity.getFuelType(), foundEngine.getFuelType());
                })
                .verifyComplete();

    }

    @Test
    void updateEngine() {

        // Ask for help
        // Save

        Engine engineEntity = buildEngine();

        String ENGINE_UUID = engineEntity.getEngineUUID();

    }

    @Test
    void deleteEngineByEngineUUID() {

        Engine engineEntity = buildEngine();

        String ENGINE_UUID = engineEntity.getEngineUUID();

        when(repo.findEngineByEngineUUID(anyString())).thenReturn(Mono.empty());

        Mono<Void> deletedObj = engineService.deleteEngine(ENGINE_UUID);

        StepVerifier.create(deletedObj)
                .expectNextCount(0)
                .verifyComplete();



    }

    @Test
    void deleteEngineByCarUUID() {

        Engine engineEntity = buildEngine();

        String CAR_UUID = engineEntity.getCarUUID();

        when(repo.findEnginesByCarUUID(anyString())).thenReturn(Flux.empty());

        Flux<Void> deletedObj = engineService.deleteEngineByCar(CAR_UUID);

        StepVerifier.create(deletedObj)
                .expectNextCount(0)
                .verifyComplete();


    }

    private Engine buildEngine(){
        return Engine.builder().id("Id").engineUUID("EngineUUID")
                .carUUID("CarUUID").name("L5P").cylinders(8).fuelType("Diesel").price(10000).build();
    }
}