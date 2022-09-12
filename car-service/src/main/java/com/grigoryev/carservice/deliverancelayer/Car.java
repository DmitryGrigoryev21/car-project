package com.grigoryev.carservice.deliverancelayer;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    @Id
    private String id;      // private id - from database
    private String carUUID;
    private String modelName;
    private String type;
    private Integer weight;
    private Integer length;
    private Integer height;
    private Integer basePrice;

}
