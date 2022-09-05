package com.grigoryev.apigateway.services;

import lombok.*;

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

}
