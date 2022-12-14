package com.grigoryev.carservice.servicelayer;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
public class CarDTO {


    private String carUUID;
    private String modelName;
    private String type;
    private Integer weight;
    private Integer length;
    private Integer height;
    private Integer basePrice;

    public CarDTO(String carUUID, String modelName, String type, Integer weight, Integer length, Integer height, Integer basePrice) {
        this.carUUID = carUUID;
        this.modelName = modelName;
        this.type = type;
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.basePrice = basePrice;
    }

}