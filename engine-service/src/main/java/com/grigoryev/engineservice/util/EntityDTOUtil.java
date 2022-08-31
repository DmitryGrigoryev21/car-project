package com.grigoryev.engineservice.util;

import com.grigoryev.engineservice.deliverancelayer.Engine;
import com.grigoryev.engineservice.servicelayer.EngineDTO;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public class EntityDTOUtil {

    public static EngineDTO toDTO(Engine engine){
        EngineDTO engineDTO = new EngineDTO();
        BeanUtils.copyProperties(engine,engineDTO);
        return engineDTO;
    }

    public static Engine toEntity(EngineDTO engineDTO){
        Engine engine = new Engine();
        BeanUtils.copyProperties(engineDTO,engine);
        return engine;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
