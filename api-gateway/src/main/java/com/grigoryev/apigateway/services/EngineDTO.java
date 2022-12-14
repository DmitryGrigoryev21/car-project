package com.grigoryev.apigateway.services;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class EngineDTO {

    private String engineUUID;
    private String carUUID;
    private String name;
    private String fuelType;
    private Integer cylinders;
    private Integer price;

    public EngineDTO(String name, String fuelType, Integer cylinders, Integer price) {
        this.name = name;
        this.fuelType = fuelType;
        this.cylinders = cylinders;
        this.price = price;
    }
}
