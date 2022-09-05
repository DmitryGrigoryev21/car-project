package com.grigoryev.apigateway.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarServiceClient {
    private final WebClient webClient;

    public CarServiceClient(
            WebClient.Builder webClientBuilder,
            @Value("${app.car-service.host}") String carServiceHost,
            @Value("${app.car-service.port}") String carServicePort
    ) {
        this.webClient = webClientBuilder
                .baseUrl("http://" + carServiceHost + ":" + carServicePort + "/car")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Flux<CarDTO> getAllCars() {
        return this.webClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(CarDTO.class);
    }

    public Mono<CarDTO> getCarByCarUUID(String name) {
        return this.webClient.get()
                .uri("/{name}", name)
                .retrieve()
                .bodyToMono(CarDTO.class);
    }

    public Mono<CarDTO> setCar(CarDTO carDTO) {
        return this.webClient.post()
                .uri("")
                .body(Mono.just(carDTO), CarDTO.class)
                .retrieve()
                .bodyToMono(CarDTO.class);
    }

    public Mono<CarDTO> updateCar(String name, CarDTO carDTO) {
        return this.webClient.put()
                .uri("/{name}", name)
                .body(Mono.just(carDTO), CarDTO.class)
                .retrieve()
                .bodyToMono(CarDTO.class);
    }

    public Mono<Void> deleteCar(String name) {
        return this.webClient.delete()
                .uri("/{name}", name)
                .retrieve()
                .bodyToMono(Void.class);
    }
}