package com.grigoryev.engineservice.presentationlayer;

import com.grigoryev.engineservice.deliverancelayer.Engine;
import com.grigoryev.engineservice.deliverancelayer.EngineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 27017"})
@AutoConfigureWebTestClient
class EngineControllerIntegrationTest {



    @Autowired
    private WebTestClient client;

    @Autowired
    private EngineRepository repo;



    @Test
    void getAllEngines() {
    }

    @Test
    void whenEngineUUIDIsValidReturnEngine() {

        Engine engineEntity = buildEngine();
        String ENGINE_ID_OKAY_UUID = engineEntity.getEngineUUID();

        Publisher<Engine> setup = repo.deleteAll().thenMany(repo.save(engineEntity));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        client.get()
                .uri("engine/" + ENGINE_ID_OKAY_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.engineUUID").isEqualTo(engineEntity.getEngineUUID())
                .jsonPath("$.name").isEqualTo(engineEntity.getName())
                .jsonPath("$.fuelType").isEqualTo(engineEntity.getFuelType())
                .jsonPath("$.carUUID").isEqualTo(engineEntity.getCarUUID());

    }

    @Test
    void getEngineByCarUUID() {
    }

    @Test
    void insertEngine() {
    }

    @Test
    void updateEngine() {
    }

    @Test
    void deleteEngineByEngineUUID() {
    }

    @Test
    void deleteEngineByCarUUID() {
    }

    private Engine buildEngine(){
        return Engine.builder().id("Id").engineUUID("EngineUUID")
                .carUUID("CarUUID").name("L5P").cylinders(8).fuelType("Diesel").price(10000).build();
    }
}