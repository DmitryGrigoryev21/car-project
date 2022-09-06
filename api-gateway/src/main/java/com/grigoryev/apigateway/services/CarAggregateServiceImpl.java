package com.grigoryev.apigateway.services;

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

//    @Override
//    public Flux<CarAggregateDTO> getAllCarAggregates() {
//        return carServiceClient.getAllCars()
//                .map(EntityDTOUtil::toAggregateDTO)
//                .collectList()
//                .flatMapIterable(x -> {
////                    x.forEach(e -> e.setEngine(engineServiceClient.getEnginesByCarUUID(e.getCarUUID())));
//                    x.forEach(e -> engineServiceClient.getEnginesByCarUUID(e.getCarUUID())
//                            .collectList()
//                            .flatMapIterable(y -> {
//                                List<EngineDTO> engineDTOS = new ArrayList<>();
//                                engineDTOS.addAll(y);
//                                System.out.println(engineDTOS.get(0).getFuelType());
//                                e.setEngine(engineDTOS);
//                                return y;
//                            }));
//                    return x;
//                });
//    }

//    @Override
//    public Flux<CarAggregateDTO> getAllCarAggregates() {
//        List<CarAggregateDTO> carAggregateDTOS = carServiceClient.getAllCars().map(EntityDTOUtil::toAggregateDTO).collectList().block();
//        for (CarAggregateDTO x : carAggregateDTOS){
//            List<EngineDTO> engineDTOS = engineServiceClient.getEnginesByCarUUID(x.getCarUUID()).collectList().block();
//            x.setEngine(engineDTOS);
//        }
//        return Flux.fromIterable(carAggregateDTOS);
//    }

    @Override
    public Flux<CarAggregateDTO> getAllCarAggregates() {
        return carServiceClient.getAllCars()
                .map(EntityDTOUtil::toAggregateDTO)
                .doOnNext(p -> p.setEngine(engineServiceClient.getEnginesByCarUUID(p.getCarUUID()).doOnNext(l -> System.out.println(l.getFuelType()))));
    }

//    @Override
//    public Mono<CarAggregateDTO> getCarAggregate(String carUUID) {
//        return carServiceClient.getCarByCarUUID(carUUID)
//                .map(EntityDTOUtil::toAggregateDTO)
//                .doOnNext(e -> engineServiceClient.getEnginesByCarUUID(e.getCarUUID())
//                        .collectList()
//                        .flatMapIterable(y -> {
//                            List<EngineDTO> engineDTOS = new ArrayList<>();
//                            engineDTOS.addAll(y);
//                            e.setEngine(engineDTOS);
//                            return y;
//                        }));
//    }

    @Override
    public Mono<CarAggregateDTO> getCarAggregate(String carUUID) {
        return carServiceClient.getCarByCarUUID(carUUID)
                .map(EntityDTOUtil::toAggregateDTO)
                .doOnNext(p -> p.setEngine(engineServiceClient.getEnginesByCarUUID(carUUID).doOnNext(l -> System.out.println(l.getFuelType()))));
    }


//    @Override
//    public Mono<CarAggregateDTO> setCarAggregate(Mono<CarAggregateDTO> carAggregateDTOMono) {
//        return carAggregateDTOMono
//                .map(EntityDTOUtil::toNonAggregateDTO)
//                .doOnNext(carServiceClient::setCar)
//                .map(EntityDTOUtil::toAggregateDTO)
//                .doOnNext(e -> engineServiceClient.setEngine(e.getEngine().collectList().block()));
//    }


// todo update


    @Override
    public Mono<Void> deleteCarAggregate(String carUUID){
        engineServiceClient.deleteEngineByCar(carUUID);
        return carServiceClient.deleteCar(carUUID);
    }
}
