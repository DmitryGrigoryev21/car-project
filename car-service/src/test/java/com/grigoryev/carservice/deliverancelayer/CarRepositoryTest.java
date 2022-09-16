package com.grigoryev.carservice.deliverancelayer;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;


@DataMongoTest
class CarRepositoryTest {

    @Autowired
    private CarRepository repo;


    @Test
    void shouldSaveOneCar() {

        Publisher<Car> setup = repo.deleteAll().thenMany(repo.save(buildCar()));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldFindCarByCarUUID(){

        Car car = buildCar();

        Publisher<Car> setup = repo.deleteAll().thenMany(repo.save(car));

        Publisher<Car> test = repo.findCarByCarUUID(car.getCarUUID());

        Publisher<Car> test2 = Flux.from(setup).thenMany(test);

        StepVerifier
                .create(test2)
                .expectNext(car)
                .verifyComplete();
    }

    @Test
    void shouldDeleteCarByCarUUID() {

        Car car = buildCar();

        repo.save(car);

        Publisher<Void> setup = repo.deleteCarByCarUUID(car.getCarUUID());

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();
    }

    private Car buildCar(){
        return Car.builder().id("Id").carUUID("CarUUID").modelName("Ninja 400").type("Motorcycle").weight(100).length(7).height(3).basePrice(6500).build();
    }

    // Should I add more???
}