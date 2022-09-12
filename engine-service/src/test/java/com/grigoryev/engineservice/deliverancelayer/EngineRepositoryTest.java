package com.grigoryev.engineservice.deliverancelayer;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


@DataMongoTest
class EngineRepositoryTest {

    @Autowired
    private EngineRepository repo;


    @Test
    void shouldSaveOneEngine() {

        Publisher<Engine> setup = repo.deleteAll().thenMany(repo.save(buildEngine()));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    void shouldFindEnginesByCarUUID() {

        Engine engine = buildEngine();

        Publisher<Engine> setup = repo.deleteAll().thenMany(repo.save(engine));

        Publisher<Engine> test = repo.findEnginesByCarUUID(engine.getCarUUID());

        Publisher<Engine> test2 = Flux.from(setup).thenMany(test);

        StepVerifier
                .create(test2)
                .expectNext(engine)
                .verifyComplete();
    }


    @Test
    void shouldFindEngineByEngineUUID() {

        Engine engine = buildEngine();

        Publisher<Engine> setup = repo.deleteAll().thenMany(repo.save(engine));

        Publisher<Engine> test = repo.findEngineByEngineUUID(engine.getEngineUUID());

        Publisher<Engine> test2 = Flux.from(setup).thenMany(test);

        StepVerifier
                .create(test2)
                .expectNext(engine)
                .verifyComplete();
    }

    @Test
    void shouldDeleteEngineByEngineUUID() {

        Engine engine = buildEngine();

        repo.save(engine);

        Publisher<Void> setup = repo.deleteEngineByEngineUUID(engine.getEngineUUID());

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldDeleteEngineByCarUUID() {

        Engine engine = buildEngine();

        repo.save(engine);

        Publisher<Void> setup = repo.deleteEngineByCarUUID(engine.getCarUUID());

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();
    }

    private Engine buildEngine(){
        return Engine.builder().id("Id").engineUUID("EngineUUID")
                .carUUID("CarUUID").name("L5P").cylinders(8).fuelType("Diesel").price(10000).build();
    }
}