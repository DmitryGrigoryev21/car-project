package com.grigoryev.engineservice.servicelayer;

import com.grigoryev.engineservice.deliverancelayer.EngineRepository;
import com.grigoryev.engineservice.util.EntityDTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EngineServiceImpl implements EngineService {

    @Autowired
    EngineRepository repository;

    @Override
    public Flux<EngineDTO> getAll(){
        return repository.findAll()

                .map(EntityDTOUtil::toDTO);
    }

    @Override
    public Mono<EngineDTO> insertEngine(Mono<EngineDTO> engineDTOMono){
        System.out.println("Engine service: insertEngine: " + engineDTOMono);
        return engineDTOMono
                .map(EntityDTOUtil::toEntity)
                .doOnNext(e -> e.setEngineUUID(EntityDTOUtil.generateUUID()))
                .flatMap(repository::insert)
                .map(EntityDTOUtil::toDTO);
    }

    @Override
    public Mono<EngineDTO> getEngineByEngineUUID(String engineUUID) {
        return repository.findEngineByEngineUUID(engineUUID)
                .map(EntityDTOUtil::toDTO);
    }

    @Override
    public Flux<EngineDTO> getEnginesByCarUUID(String carUUID) {
        return repository.findEnginesByCarUUID(carUUID)
                .map(EntityDTOUtil::toDTO);
    }

    @Override
    public Mono<EngineDTO> updateEngine(String engineUUID, Mono<EngineDTO> engineDTOMono) {

        return repository.findEngineByEngineUUID(engineUUID)
                .flatMap(p -> engineDTOMono
                        .map(EntityDTOUtil::toEntity)
                        .doOnNext(e -> e.setEngineUUID(p.getEngineUUID()))
                        .doOnNext(e -> e.setId(p.getId()))
                )
                .flatMap(repository::save)
                .map(EntityDTOUtil::toDTO);
    }


    @Override
    public Mono<Void> deleteEngine(String engineUUID) {
        return repository.deleteEngineByEngineUUID(engineUUID);
    }

    @Override
    public Flux<Void> deleteEngineByCar(String engineUUID) {
        return repository.deleteEngineByCarUUID(engineUUID);
    }
}
