package com.grigoryev.apigateway.services;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class CarDTO {

    private String carUUID;
    private String modelName;
    private String type;
    private Integer weight;
    private Integer length;
    private Integer height;
    private Integer basePrice;

    public CarDTO(String modelName, String type, Integer weight, Integer length, Integer height, Integer basePrice) {
        this.modelName = modelName;
        this.type = type;
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.basePrice = basePrice;
    }
}
