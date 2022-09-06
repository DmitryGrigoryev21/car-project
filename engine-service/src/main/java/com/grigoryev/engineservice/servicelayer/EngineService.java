package com.grigoryev.engineservice.servicelayer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EngineService {

    public Flux<EngineDTO> getAll();

    public Mono<EngineDTO> insertEngine(Mono<EngineDTO> engineDTOMono);

    public Mono<EngineDTO> getEngineByEngineUUID(String engineUUID);

    public Flux<EngineDTO> getEnginesByCarUUID(String carUUID);

    public Mono<EngineDTO> updateEngine(String engineUUID, Mono<EngineDTO> engineDTOMono);

    public Mono<Void> deleteEngine(String engineUUID);

    public Flux<Void> deleteEngineByCar(String engineUUID);
}