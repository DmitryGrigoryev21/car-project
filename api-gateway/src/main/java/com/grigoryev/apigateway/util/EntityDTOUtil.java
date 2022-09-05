package com.grigoryev.apigateway.util;

import com.grigoryev.apigateway.aggregates.CarAggregate;
import com.grigoryev.apigateway.services.CarAggregateDTO;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public class EntityDTOUtil {

    public static CarAggregateDTO toDTO(CarAggregate carAggregate){
        CarAggregateDTO carAggregateDTO = new CarAggregateDTO();
        BeanUtils.copyProperties(carAggregate,carAggregateDTO);
        return carAggregateDTO;
    }

    public static CarAggregate toEntity(CarAggregateDTO carAggregateDTO){
        CarAggregate carAggregate = new CarAggregate();
        BeanUtils.copyProperties(carAggregateDTO,carAggregate);
        return carAggregate;
    }
}
