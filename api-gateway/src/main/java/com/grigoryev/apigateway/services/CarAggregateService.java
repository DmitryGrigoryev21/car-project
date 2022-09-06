package com.grigoryev.apigateway.services;

import reactor.core.publisher.Flux;

public interface CarAggregateService {

    public Flux<CarAggregateDTO> getAllCarAggregates();
}
