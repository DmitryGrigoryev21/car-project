package com.grigoryev.carservice.util;

import com.grigoryev.carservice.deliverancelayer.Car;
import com.grigoryev.carservice.servicelayer.CarDTO;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public class EntityDTOUtil {

    public static CarDTO toDTO(Car car){
        CarDTO carDTO = new CarDTO();
        BeanUtils.copyProperties(car,carDTO);
        return carDTO;
    }

    public static Car toEntity(CarDTO carDTO){
        Car car = new Car();
        BeanUtils.copyProperties(carDTO,car);
        return car;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
