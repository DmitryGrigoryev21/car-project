package com.grigoryev.engineservice.deliverancelayer;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
public class Engine {

    @Id
    private String id;      // private id - from database
    private String engineUUID;
    private String carUUID;
    private String name;
    private String fuelType;
    private Integer cylinders;
    private Integer price;
}