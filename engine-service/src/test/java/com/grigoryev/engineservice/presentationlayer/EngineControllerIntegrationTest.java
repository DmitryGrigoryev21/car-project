package com.grigoryev.engineservice.presentationlayer;

import com.grigoryev.engineservice.deliverancelayer.Engine;
import com.grigoryev.engineservice.deliverancelayer.EngineRepository;
import com.grigoryev.engineservice.util.EntityDTOUtil;
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
import static reactor.core.publisher.Mono.just;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 27017"})
@AutoConfigureWebTestClient
class EngineControllerIntegrationTest {         // Completely Functional



    @Autowired
    private WebTestClient client;

    @Autowired
    private EngineRepository repo;



    @Test
    void getAllEngines() {


        Engine engineEntity = buildEngine();
        Publisher<Engine> setup = repo.deleteAll().thenMany(repo.save(engineEntity));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        client.get()
                .uri("/engine")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].engineUUID").isEqualTo(engineEntity.getEngineUUID())
                .jsonPath("$[0].name").isEqualTo(engineEntity.getName())
                .jsonPath("$[0].fuelType").isEqualTo(engineEntity.getFuelType())
                .jsonPath("$[0].carUUID").isEqualTo(engineEntity.getCarUUID());

    }

    @Test
    void whenEngineUUIDIsValidReturnEngine() {

        Engine engineEntity = buildEngine();
        String ENGINE_OKAY_UUID = engineEntity.getEngineUUID();

        Publisher<Engine> setup = repo.deleteAll().thenMany(repo.save(engineEntity));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        client.get()
                .uri("/engine/" + ENGINE_OKAY_UUID)
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
    void whenCarUUIDIsValidReturnEnginesByCarUUID() {


        Engine engineEntity = buildEngine();
        String CAR_UUID = engineEntity.getCarUUID();

        Publisher<Engine> setup = repo.deleteAll().thenMany(repo.save(engineEntity));


        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        client.get()
                .uri("/engine/car/" + CAR_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].name").isEqualTo(engineEntity.getName())
                .jsonPath("$[0].fuelType").isEqualTo(engineEntity.getFuelType())
                .jsonPath("$[0].carUUID").isEqualTo(engineEntity.getCarUUID());
    }

    @Test
    void shouldInsertEngineWithValidEngineObject() {

        Engine engineEntity = buildEngine();

        Publisher<Engine> setup = repo.deleteAll().thenMany(repo.save(engineEntity));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        client.post()                                                                   // Inserting the object
                .uri("/engine")
                .body(just(engineEntity), Engine.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();

        client.get()                                                                    // Checking if the item was posted properly
                .uri("/engine/" + engineEntity.getEngineUUID())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo(engineEntity.getName())
                .jsonPath("$.fuelType").isEqualTo(engineEntity.getFuelType())
                .jsonPath("$.carUUID").isEqualTo(engineEntity.getCarUUID());


    }


    @Test
    void whenEngineUUIDIsValidUpdateEngine() {

        Engine engineEntity = buildEngine();
        Engine engineEntity2 = buildEngine2();
        engineEntity2.setEngineUUID("EngineUUID");
        engineEntity2.setCarUUID("CarUUID");
        engineEntity2.setId("Id");

        Publisher<Engine> setup = repo.deleteAll().thenMany(repo.save(engineEntity));

        StepVerifier                    // Setting up
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        client.get()                                                                    // Checking the original item
                .uri("/engine/" + engineEntity.getEngineUUID())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo(engineEntity.getName())
                .jsonPath("$.fuelType").isEqualTo(engineEntity.getFuelType())
                .jsonPath("$.price").isEqualTo(engineEntity.getPrice());

        client.put()
                .uri("/engine/" + engineEntity.getEngineUUID())
                .body(just(engineEntity2), Engine.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo(engineEntity2.getName());

        client.get()                                                                    // Checking if the item was updated properly
                .uri("/engine/" + engineEntity.getEngineUUID())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo(engineEntity2.getName())
                .jsonPath("$.fuelType").isEqualTo(engineEntity2.getFuelType())
                .jsonPath("$.price").isEqualTo(engineEntity2.getPrice());

    }




    @Test
    void deleteEngineByEngineUUID() {

        Engine entity = buildEngine();

        repo.save(entity);

        Publisher<Void> setup = repo.deleteEngineByEngineUUID(buildEngine().getEngineUUID());

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();

        client.delete()
                .uri("/engine/" + entity.getEngineUUID())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();


    }

    @Test
    void deleteEngineByCarUUID() {

        Engine entity = buildEngine();

        repo.save(entity);

        Publisher<Void> setup = repo.deleteEngineByEngineUUID(buildEngine().getCarUUID());

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();

        client.delete()
                .uri("/engine/car/" + entity.getCarUUID())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }

    private Engine buildEngine(){
        return Engine.builder().id("Id").engineUUID("EngineUUID")
                .carUUID("CarUUID").name("L5P").cylinders(8).fuelType("Diesel").price(10000).build();
    }

    private Engine buildEngine2(){
        return Engine.builder().id("Id2").engineUUID("EngineUUID2")
                .carUUID("CarUUID2").name("LB7").cylinders(8).fuelType("Diesel").price(5000).build();
    }
}