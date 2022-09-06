package com.grigoryev.carservice.servicelayer;

import com.grigoryev.carservice.deliverancelayer.CarRepository;
import com.grigoryev.carservice.util.EntityDTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    CarRepository repository;

    @Override
    public Flux<CarDTO> getAll(){
        return repository.findAll()
                .map(EntityDTOUtil::toDTO);
    }

    @Override
    public Mono<CarDTO> insertCar(Mono<CarDTO> carDTOMono){
        return carDTOMono
                .map(EntityDTOUtil::toEntity)
                .doOnNext(e -> e.setCarUUID(EntityDTOUtil.generateUUID()))
                .flatMap(repository::insert)
                .map(EntityDTOUtil::toDTO);
    }

    @Override
    public Mono<CarDTO> getCarByCarUUID(String carUUID) {
        return repository.findCarByCarUUID(carUUID)
                .map(EntityDTOUtil::toDTO);
    }


    @Override
    public Mono<CarDTO> updateCar(String carUUID, Mono<CarDTO> carDTOMono) {

        return repository.findCarByCarUUID(carUUID)
                .flatMap(p -> carDTOMono
                        .map(EntityDTOUtil::toEntity)
                        .doOnNext(e -> e.setCarUUID(p.getCarUUID()))
                        .doOnNext(e -> e.setModelName(p.getModelName()))
                        .doOnNext(e -> e.setType(p.getType()))
                        .doOnNext(e -> e.setWeight(p.getWeight()))
                        .doOnNext(e -> e.setHeight(p.getHeight()))
                        .doOnNext(e -> e.setLength(p.getLength()))
                        .doOnNext(e -> e.setBasePrice(p.getBasePrice()))
                )
                .flatMap(repository::save)
                .map(EntityDTOUtil::toDTO);
    }


    @Override
    public Mono<Void> deleteCar(String carUUID) {
        return repository.deleteCarByCarUUID(carUUID);
    }
}
