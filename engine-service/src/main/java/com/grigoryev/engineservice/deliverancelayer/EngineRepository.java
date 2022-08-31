package com.grigoryev.engineservice.deliverancelayer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EngineRepository extends ReactiveMongoRepository<Engine, String> {
    Mono<Engine> findEngineByEngineUUID(String engineUUID);
    Flux<Engine> findEnginesByCarUUID(String carUUID);
    Mono<Void> deleteEngineByEngineUUID(String engineUUID);
}
