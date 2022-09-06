package com.grigoryev.apigateway.util;

import com.grigoryev.apigateway.aggregates.CarAggregate;
import com.grigoryev.apigateway.services.CarAggregateDTO;
import com.grigoryev.apigateway.services.CarDTO;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public class EntityDTOUtil {

    public static CarAggregateDTO toDTO(CarAggregate carAggregate){
        CarAggregateDTO carAggregateDTO = new CarAggregateDTO();
        BeanUtils.copyProperties(carAggregate,carAggregateDTO);
        return carAggregateDTO;
    }

    public static CarAggregateDTO toAggregateDTO(CarDTO carDTO){
        CarAggregateDTO carAggregateDTO = new CarAggregateDTO();
        BeanUtils.copyProperties(carDTO, carAggregateDTO);
        return carAggregateDTO;
    }

    public static CarDTO toNonAggregateDTO(CarAggregateDTO carAggregateDTO){
        CarDTO carDTO = new CarDTO();
        BeanUtils.copyProperties(carAggregateDTO, carDTO);
        return carDTO;
    }

    public static CarAggregate toEntity(CarAggregateDTO carAggregateDTO){
        CarAggregate carAggregate = new CarAggregate();
        BeanUtils.copyProperties(carAggregateDTO,carAggregate);
        return carAggregate;
    }
}
