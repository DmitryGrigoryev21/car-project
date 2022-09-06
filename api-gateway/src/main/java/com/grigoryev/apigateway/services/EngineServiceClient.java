package com.grigoryev.apigateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EngineServiceClient {

    private final WebClient webClient;

    public EngineServiceClient(
            WebClient.Builder webClientBuilder,
            @Value("${app.engine-service.host}") String engineServiceHost,
            @Value("${app.engine-service.port}") String engineServicePort
    ) {
        this.webClient = webClientBuilder
                .baseUrl("http://" + engineServiceHost + ":" + engineServicePort + "/engine")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Flux<EngineDTO> getAllEngines() {
        return this.webClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(EngineDTO.class);
    }

    public Mono<EngineDTO> getEngineByEngineUUID(String name) {
        return this.webClient.get()
                .uri("/{name}", name)
                .retrieve()
                .bodyToMono(EngineDTO.class);
    }

    public Flux<EngineDTO> getEnginesByCarUUID(String name) {
        return this.webClient.get()
                .uri("/car/{name}", name)
                .retrieve()
                .bodyToFlux(EngineDTO.class);
    }

    public Mono<EngineDTO> setEngine(EngineDTO engineDTO) {
        return this.webClient.post()
                .uri("")
                .body(Mono.just(engineDTO), EngineDTO.class)
                .retrieve()
                .bodyToMono(EngineDTO.class);
    }

    public Mono<EngineDTO> updateEngine(String name,EngineDTO engineDTO) {
        return this.webClient.put()
                .uri("/{name}", name)
                .body(Mono.just(engineDTO), EngineDTO.class)
                .retrieve()
                .bodyToMono(EngineDTO.class);
    }

    public Mono<Void> deleteEngine(String name) {
        return this.webClient.delete()
                .uri("/{name}")
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Flux<Void> deleteEngineByCar(String name) {
        return this.webClient.delete()
                .uri("/car/{name}", name)
                .retrieve()
                .bodyToFlux(Void.class);
    }
}
