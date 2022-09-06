package com.grigoryev.apigateway.util;

import com.grigoryev.apigateway.services.CarAggregateDTO;
import com.grigoryev.apigateway.services.CarDTO;
import com.grigoryev.apigateway.services.EngineDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ContextUtil {
    List<EngineDTO> engineDTOS;
    CarAggregateDTO carAggregateDTO;

    public ContextUtil(CarAggregateDTO carAggregateDTO){
        this.carAggregateDTO =carAggregateDTO;
    }

}
