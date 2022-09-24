package com.grigoryev.carservice.presentationlayer;

import com.grigoryev.carservice.deliverancelayer.Car;
import com.grigoryev.carservice.deliverancelayer.CarRepository;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import static reactor.core.publisher.Mono.just;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 27017"})
@AutoConfigureWebTestClient
class CarControllerIntegrationTest {


    @Autowired
    private WebTestClient client;

    @Autowired
    private CarRepository repo;


    @Test
    void getAllCars() {

        Car carEntity = buildCar();

        Publisher<Car> setup = repo.deleteAll().thenMany(repo.save(carEntity));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        client.get()
                .uri("/car")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].modelName").isEqualTo(carEntity.getModelName())
                .jsonPath("[0].type").isEqualTo(carEntity.getType())
                .jsonPath("[0].carUUID").isEqualTo(carEntity.getCarUUID());
    }

    @Test
    void whenValidCarUUIDReturnCar() {

        Car carEntity = buildCar();

        String CAR_UUID_OK = carEntity.getCarUUID();

        Publisher<Car> setup = repo.deleteAll().thenMany(repo.save(carEntity));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        client.get()
                .uri("/car/" + CAR_UUID_OK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.modelName").isEqualTo(carEntity.getModelName())
                .jsonPath("$.type").isEqualTo(carEntity.getType())
                .jsonPath("$.carUUID").isEqualTo(carEntity.getCarUUID());
    }

    @Test
    void shouldInsertCarWithValidCarObject() {

        Car carEntity = buildCar();
        String CAR_UUID_OK = carEntity.getCarUUID();

        Publisher<Car> setup = repo.deleteAll().thenMany(repo.save(carEntity));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        client.post()
                .uri("/car")
                .body(just(carEntity), Car.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();

        client.get()
                .uri("/car/" + CAR_UUID_OK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.modelName").isEqualTo(carEntity.getModelName())
                .jsonPath("$.type").isEqualTo(carEntity.getType())
                .jsonPath("$.carUUID").isEqualTo(carEntity.getCarUUID());
    }

    @Test
    void whenCarUUIDIsValidUpdateCar() {

        Car carEntity = buildCar();
        Car carEntity2 = buildCar();
        carEntity2.setCarUUID("CarUUID2");
        carEntity2.setId("Id2");
        carEntity2.setModelName("Ninja 650");
        String CAR_UUID_OK = carEntity.getCarUUID();

        Publisher<Car> setup = repo.deleteAll().thenMany(repo.save(carEntity));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        client.get()
                .uri("/car/" + CAR_UUID_OK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.modelName").isEqualTo(carEntity.getModelName())
                .jsonPath("$.type").isEqualTo(carEntity.getType())
                .jsonPath("$.carUUID").isEqualTo(carEntity.getCarUUID());

        client.put()
                .uri("/car/" + CAR_UUID_OK)
                .body(just(carEntity2), Car.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.modelName").isEqualTo(carEntity2.getModelName());

        client.get()
                .uri("/car/" + CAR_UUID_OK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.modelName").isEqualTo(carEntity2.getModelName())       // Checks if the value is equal to the updated value
                .jsonPath("$.type").isEqualTo(carEntity.getType())
                .jsonPath("$.carUUID").isEqualTo(carEntity.getCarUUID());


    }

    @Test
    void deleteCarByCarUUID() {

        Car carEntity = buildCar();

        repo.save(carEntity);

        Publisher<Void> setup = repo.deleteCarByCarUUID(carEntity.getCarUUID());

        StepVerifier.create(setup)
                .expectNextCount(0)
                .verifyComplete();

        client.delete()
                .uri("/car/" + carEntity.getCarUUID())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }


    private Car buildCar(){
        return Car.builder().id("Id").carUUID("CarUUID").modelName("Ninja 400").type("Motorcycle").weight(100).length(7).height(3).basePrice(6500).build();
    }
}