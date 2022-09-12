package com.grigoryev.apigateway.services;

import com.grigoryev.apigateway.util.ContextUtil;
import com.grigoryev.apigateway.util.EntityDTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CarAggregateServiceImpl implements CarAggregateService{

    private final CarServiceClient carServiceClient;
    private final EngineServiceClient engineServiceClient;

    @Autowired
    public CarAggregateServiceImpl(CarServiceClient carServiceClient, EngineServiceClient engineServiceClient) {
        this.carServiceClient = carServiceClient;
        this.engineServiceClient = engineServiceClient;
    }

    @Override
    public Mono<CarAggregateDTO> getCarAggregate(String carUUID) {

        return carServiceClient.getCarByCarUUID(carUUID)
                .map(EntityDTOUtil::toAggregateDTO)
                .flatMap(x -> engineServiceClient.getEnginesByCarUUID(x.getCarUUID())
                        .collectList()
                        .map((list) -> {
                            return new CarAggregateDTO(x.getCarUUID(),
                                    x.getModelName(),
                                    x.getType(),
                                    x.getWeight(),
                                    x.getLength(),
                                    x.getHeight(),
                                    x.getBasePrice(),
                                    list);}
                        ));
    }

    @Override
    public Flux<CarAggregateDTO> getAllCarAggregates() {

        return carServiceClient.getAllCars()
                .map(EntityDTOUtil::toAggregateDTO)
                .flatMap(x -> engineServiceClient.getEnginesByCarUUID(x.getCarUUID())
                        .collectList()
                        .map((list) -> {
                            return new CarAggregateDTO(x.getCarUUID(),
                                    x.getModelName(),
                                    x.getType(),
                                    x.getWeight(),
                                    x.getLength(),
                                    x.getHeight(),
                                    x.getBasePrice(),
                                    list);}
                        ));
    }

    @Override
    public Mono<CarAggregateDTO> setCarAggregate(CarAggregateDTO carAggregateDTO){
        return Mono.just(carAggregateDTO)
                .map(EntityDTOUtil::toNonAggregateDTO)
                .flatMap(carServiceClient::setCar)
                .map(x -> {
                    carAggregateDTO.setCarUUID(x.getCarUUID());
                    return carAggregateDTO;
                })
                .flatMap(x -> Mono.just(x.getEngine()))
                .doOnNext(x -> x.forEach(y -> y.setCarUUID(carAggregateDTO.getCarUUID())))
                .flatMapMany(Flux::fromIterable)
                .flatMap(engineServiceClient::setEngine)
                .collectList()
                .map(x -> {
                    carAggregateDTO.setEngine(x);
                    return carAggregateDTO;
                });
    }

    @Override
    public Mono<CarAggregateDTO> updateCarAggregate(CarAggregateDTO carAggregateDTO, String carUUID){
        return carServiceClient.getCarByCarUUID(carUUID)
                .flatMap(x -> carServiceClient.updateCar(carUUID, EntityDTOUtil.toNonAggregateDTO(carAggregateDTO)))
                .map(x -> carAggregateDTO)
                .map(CarAggregateDTO::getEngine)
                .flatMapMany(Flux::fromIterable)
                .flatMap(x -> engineServiceClient.updateEngine(carUUID, x))
                .collectList()
                .map(x -> carAggregateDTO);
        //test
    }

    @Override
    public Mono<EngineDTO> test(String engineUUID, EngineDTO engineDTO){
        return engineServiceClient.updateEngine(engineUUID,engineDTO);
    }

    // todo make params mono

    @Override
    public Mono<Void> deleteCarAggregate(String carUUID){
        engineServiceClient.deleteEngineByCar(carUUID);
        return carServiceClient.deleteCar(carUUID);
    }
}
