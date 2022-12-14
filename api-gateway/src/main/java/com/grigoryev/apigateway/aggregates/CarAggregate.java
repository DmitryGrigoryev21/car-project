package com.grigoryev.apigateway.aggregates;

import com.grigoryev.apigateway.services.EngineDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class CarAggregate {

    private String id;
    private String carUUID;
    private String modelName;
    private String type;
    private Integer weight;
    private Integer length;
    private Integer height;
    private Integer basePrice;
    private List<EngineDTO> engine; //may need to change to flux

}
