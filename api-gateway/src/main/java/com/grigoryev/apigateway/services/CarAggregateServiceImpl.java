package com.grigoryev.apigateway.services;

import com.grigoryev.apigateway.util.EntityDTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Flux<CarAggregateDTO> getAllCarAggregates() {
        return carServiceClient.getAllCars()
                .map(EntityDTOUtil::toAggregateDTO)
                .collectList()
                .flatMapIterable(x -> {
                    x.forEach(e -> e.setEngine(engineServiceClient.getEnginesByCarUUID(e.getCarUUID())));
                    return x;
                });
    }

    @Override
    public Mono<CarAggregateDTO> getCarAggregate(String carUUID) {
        return carServiceClient.getCarByCarUUID(carUUID)
                .map(EntityDTOUtil::toAggregateDTO)
                .doOnNext(e -> e.setEngine(engineServiceClient.getEnginesByCarUUID(e.getCarUUID())));
    }

//    @Override
//    public Mono<CarAggregateDTO> setCarAggregate(Mono<CarAggregateDTO> carAggregateDTOMono) {
//        return carAggregateDTOMono.flatMap(x -> {
//            CarDTO carDTO = new CarDTO(x.getCarUUID(),x.getModelName(), x.getType(), x.getWeight(), x.getLength(), x.getHeight(), x.getBasePrice());
//            carServiceClient.setCar(carDTO);
//            EngineDTO engineDTO = new EngineDTO();
//            engineDTO.setCylinders(x.getEngine().flatMap(k -> k.getCylinders()));
//        });
//    }
}
