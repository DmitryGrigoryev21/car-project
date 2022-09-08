package com.grigoryev.apigateway.services;

import lombok.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class CarAggregateDTO {

    private String carUUID;
    private String modelName;
    private String type;
    private Integer weight;
    private Integer length;
    private Integer height;
    private Integer basePrice;
    private List<EngineDTO> engine; //may need to change to flux

    public CarAggregateDTO(String carUUID, String modelName, String type, Integer weight, Integer length, Integer height, Integer basePrice, List<EngineDTO> engine) {
        this.carUUID = carUUID;
        this.modelName = modelName;
        this.type = type;
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.basePrice = basePrice;
        this.engine = engine;
    }
}
