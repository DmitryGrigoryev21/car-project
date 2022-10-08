package com.grigoryev.engineservice.servicelayer;

import com.grigoryev.engineservice.deliverancelayer.Engine;
import com.grigoryev.engineservice.deliverancelayer.EngineRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.ArgumentMatchers.any;

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


        Engine engineEntity = buildEngine();

        when(repo.findAll()).thenReturn(Flux.just(engineEntity));

        Flux<EngineDTO> engineDTOMono = engineService.getAll();

        StepVerifier.create(engineDTOMono)
                .consumeNextWith(foundEngine ->{
                    assertNotNull(foundEngine);
                })
                .verifyComplete();

    }

    @Test
    void insertEngine() {

        Engine engineEntity = buildEngine();

        Mono<Engine> engineMono = Mono.just(engineEntity);

        EngineDTO dtoObj = buildEngineDTO();

        when(repo.insert(any(Engine.class))).thenReturn(engineMono);

        Mono<EngineDTO> returnedEngine = engineService.insertEngine(Mono.just(dtoObj));

        StepVerifier.create(returnedEngine)
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
    void updateEngine() {


        Engine engineEntity = buildEngine();
        String ENGINE_UUID = engineEntity.getEngineUUID();
        EngineDTO dto = buildEngineDTO();
        dto.setName("UpdatedEngine");

        Engine updatedEngine = new Engine();
        BeanUtils.copyProperties(engineEntity, updatedEngine);
        updatedEngine.setName(dto.getName());

        when(repo.findEngineByEngineUUID(anyString())).thenReturn(Mono.just(engineEntity));
        when(repo.save(any(Engine.class))).thenReturn(Mono.just(updatedEngine));

        Mono<EngineDTO> engineDTOMono = engineService.updateEngine(ENGINE_UUID, Mono.just(dto));


        StepVerifier.create(engineDTOMono)
                .consumeNextWith(foundEngine -> {
                    assertNotEquals(engineEntity.getName(), foundEngine.getName());
                })
                .verifyComplete();

    }

    @Test
    void deleteEngineByEngineUUID() {

        Engine engineEntity = buildEngine();

        String ENGINE_UUID = engineEntity.getEngineUUID();

        when(repo.deleteEngineByEngineUUID(anyString())).thenReturn(Mono.empty());

        Mono<Void> deletedObj = engineService.deleteEngine(ENGINE_UUID);

        StepVerifier.create(deletedObj)
                .expectNextCount(0)
                .verifyComplete();

    }

    @Test
    void deleteEngineByCarUUID() {

        Engine engineEntity = buildEngine();

        String CAR_UUID = engineEntity.getCarUUID();

        when(repo.deleteEngineByCarUUID(anyString())).thenReturn(Flux.empty());

        Flux<Void> deletedObj = engineService.deleteEngineByCar(CAR_UUID);

        StepVerifier.create(deletedObj)
                .expectNextCount(0)
                .verifyComplete();


    }

    private Engine buildEngine(){
        return Engine.builder().id("Id").engineUUID("EngineUUID")
                .carUUID("CarUUID").name("L5P").cylinders(8).fuelType("Diesel").price(10000).build();
    }

    private Engine buildEngine2(){
        return Engine.builder().id("Id2").engineUUID("EngineUUID2")
                .carUUID("CarUUID2").name("LB7").cylinders(8).fuelType("Diesel").price(5000).build();
    }


    private EngineDTO buildEngineDTO(){
        return EngineDTO.builder().engineUUID("EngineUUID")
                .carUUID("CarUUID").name("SwagEngine").cylinders(8).fuelType("Swag").price(15000).build();
    }
}