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
    public Flux<CarAggregateDTO> getAllCarAggregates() {
        return carServiceClient.getAllCars()
                .map(EntityDTOUtil::toAggregateDTO)
                .collectList()
                .flatMapIterable(x -> {
//                    x.forEach(e -> e.setEngine(engineServiceClient.getEnginesByCarUUID(e.getCarUUID())));
                    x.forEach(e -> engineServiceClient.getEnginesByCarUUID(e.getCarUUID())
                            .collectList()
                            .flatMapIterable(y -> {
                                List<EngineDTO> engineDTOS = new ArrayList<>();
                                engineDTOS.addAll(y);
                                System.out.println(engineDTOS.get(0).getFuelType());
                                e.setEngine(engineDTOS);
                                return y;
                            }));
                    return x;
                });
    }

//    @Override
//    public Mono<CarAggregateDTO> getAllCarAggregates() {
//        return carServiceClient.getAllCars()
//                .map(EntityDTOUtil::toAggregateDTO)
//                .doOnNext(m -> m.getEngine().forEach(n -> n.))
//    }

//    @Override
//    public Flux<CarAggregateDTO> getAllCarAggregates() {
//        return carServiceClient.getAllCars()
//                .map(EntityDTOUtil::toAggregateDTO)
//                .doOnNext(p -> p.setEngine(engineServiceClient.getEnginesByCarUUID(p.getCarUUID()).doOnNext(l -> System.out.println(l.getFuelType()))));
//    }

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

//    @Override
//    public Mono<CarAggregateDTO> getCarAggregate(String carUUID) {
//        return carServiceClient.getCarByCarUUID(carUUID)
//                .map(EntityDTOUtil::toAggregateDTO)
////                .doOnNext(p -> p.setEngine(engineServiceClient.getEnginesByCarUUID(carUUID).doOnNext(l -> System.out.println(l.getFuelType()))));
//                .doOnNext(p -> p.setEngine(engineServiceClient.getEnginesByCarUUID(carUUID).collectList());
//    }

    @Override
    public Mono<CarAggregateDTO> getCarAggregate(String carUUID) {
        return this.itrysohard(carUUID)
                .flatMap(this::itry)
                .map(EntityDTOUtil::getAggregateDTO);



//        .getCarByCarUUID(carUUID)
//                .map(EntityDTOUtil::toAggregateDTO)
//                .flatMap(this::itry)
//                .map(EntityDTOUtil::getAggregateDTO);
    }

    public Mono<ContextUtil> itry(ContextUtil contextUtil){
        return engineServiceClient.getEnginesByCarUUID(contextUtil.getCarAggregateDTO().getCarUUID())
                .log()
                .collectList()
                .doOnNext(contextUtil::setEngineDTOS)
                .thenReturn(contextUtil);
    }

    public Mono<ContextUtil> itrysohard(String carUUID){
        return carServiceClient.getCarByCarUUID(carUUID)
                .map(EntityDTOUtil::toAggregateDTO)
                .map(ContextUtil::new)
                .doOnNext(System.out::println);
    }


//    @Override
//    public Mono<CarAggregateDTO> setCarAggregate(Mono<CarAggregateDTO> carAggregateDTOMono) {
//        return carAggregateDTOMono
//                .map(EntityDTOUtil::toNonAggregateDTO)
//                .doOnNext(carServiceClient::setCar)
//                .map(EntityDTOUtil::toAggregateDTO)
//                .doOnNext(e -> engineServiceClient.setEngine(e.getEngine().collectList().block()));
//    }
    @Override
    public Flux<EngineDTO> test(String carUUID) {
        return engineServiceClient.getEnginesByCarUUID(carUUID);
    }

//    @Override
//    public Flux<CarAggregateDTO> getAllCarAggregates() {
//        Mono<List<CarAggregateDTO>> list1 = carServiceClient.getAllCars().map(EntityDTOUtil::toAggregateDTO).collectList();
//        list1.flatMap(n -> n.forEach(m -> {
//            Mono<List<EngineDTO>> list2 = engineServiceClient.getEnginesByCarUUID(m.getCarUUID()).collectList();
//        }));
//    }


// todo update


    @Override
    public Mono<Void> deleteCarAggregate(String carUUID){
        engineServiceClient.deleteEngineByCar(carUUID);
        return carServiceClient.deleteCar(carUUID);
    }
}
