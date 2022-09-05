package com.grigoryev.carservice.deliverancelayer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CarRepository extends ReactiveMongoRepository<Car, String> {

    Mono<Car> findCarByCarUUID(String carUUID);
    Mono<Void> deleteCarByCarUUID(String carUUID);
}
