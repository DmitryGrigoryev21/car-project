package com.grigoryev.engineservice.servicelayer;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EngineDTO {


    private String engineUUID;
    private String carUUID;
    private String name;
    private String fuelType;
    private Integer cylinders;
    private Integer price;

    public EngineDTO(String carUUID, String name, String fuelType,Integer cylinders, Integer price) {
        this.carUUID = carUUID;
        this.name = name;
        this.fuelType = fuelType;
        this.cylinders = cylinders;
        this.price = price;
    }

}