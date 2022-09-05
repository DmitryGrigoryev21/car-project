package com.grigoryev.carservice.servicelayer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarService {

    public Flux<CarDTO> getAll();

    public Mono<CarDTO> insertCar(Mono<CarDTO> carDTOMono);

    public Mono<CarDTO> getCarByCarUUID(String carUUID);

    public Mono<CarDTO> updateCar(String carUUID, Mono<CarDTO> carDTOMono);

    public Mono<Void> deleteCar(String carUUID);
}