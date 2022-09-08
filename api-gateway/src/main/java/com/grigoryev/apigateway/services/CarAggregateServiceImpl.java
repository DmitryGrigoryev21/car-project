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

        return this.carServiceClient.getCarByCarUUID(carUUID)
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

        return this.carServiceClient.getAllCars()
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
        CarDTO carDTO = new CarDTO(
                carAggregateDTO.getModelName(),
                carAggregateDTO.getType(),
                carAggregateDTO.getWeight(),
                carAggregateDTO.getLength(),
                carAggregateDTO.getHeight(),
                carAggregateDTO.getBasePrice());
        carServiceClient.setCar(carDTO)
                .doOnNext(x -> {
                    String newCarUUID = x.getCarUUID();
                    List<EngineDTO> engineDTOS = carAggregateDTO.getEngine();
                    for (EngineDTO e : engineDTOS){
                        e.setCarUUID(newCarUUID);
                        engineServiceClient.setEngine(e);
                    }
                });
        return Mono.just(carAggregateDTO);
    }

    // todo update

    @Override
    public Mono<Void> deleteCarAggregate(String carUUID){
        engineServiceClient.deleteEngineByCar(carUUID);
        return carServiceClient.deleteCar(carUUID);
    }
}
