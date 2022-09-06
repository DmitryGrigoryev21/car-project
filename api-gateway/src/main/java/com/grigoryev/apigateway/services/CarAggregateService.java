package com.grigoryev.apigateway.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarAggregateService {

    public Flux<CarAggregateDTO> getAllCarAggregates();
    public Mono<CarAggregateDTO> getCarAggregate(String carUUID);
    public Mono<Void> deleteCarAggregate(String carUUID);
//    public Mono<CarAggregateDTO> setCarAggregate(Mono<CarAggregateDTO> carAggregateDTOMono);
}
