package com.grigoryev.apigateway.controllers;

import com.grigoryev.apigateway.services.CarAggregateDTO;
import com.grigoryev.apigateway.services.CarAggregateService;
import com.grigoryev.apigateway.services.EngineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class CarAggregateController {

    @Autowired
    private CarAggregateService carAggregateService;

    public CarAggregateController(CarAggregateService carAggregateService){
        this.carAggregateService = carAggregateService;
    }

    @GetMapping(
            value = "/car",
            produces = "application/json"
    )
    public Flux<CarAggregateDTO> getAllCarAggregates(){
        return carAggregateService.getAllCarAggregates();
    }

    @GetMapping(
            value = "/car/{carUUID}",
            produces = "application/json"
    )
    public Mono<ResponseEntity<CarAggregateDTO>> getCarByCarUUID(@PathVariable String carUUID){
        return carAggregateService.getCarAggregate(carUUID)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


//test
//    @GetMapping(
//            value = "/engine",
//            produces = "application/json"
//    )
//    public Flux<EngineDTO> getAllEngines(){
//        return carAggregateService.getEngineDTOs();
//    }
//
    @GetMapping(
            value = "/engine/car/{uuid}",
            produces = "application/json"
    )
    public Flux<EngineDTO> getAllEngines(@PathVariable String uuid){
        return carAggregateService.test(uuid);
    }
//
//
//-------

//    @PostMapping(
//            value = "/car",
//            consumes = "application/json"
//    )
//    public ResponseEntity<Flux<CarAggregateDTO>> createNewCarAggregate(@RequestBody CarAggregateDTO newAgg){
//
//        return ResponseEntity.status(HttpStatus.OK).body(carAggregateService.createAggregate(newAgg));
//    }
//
//    @PutMapping(
//            value = "/car/{caruuid}",
//            consumes = "application/json"
//    )
//    public ResponseEntity<Flux<CarAggregateDTO>> updateAggregate(@RequestBody CarAggregateDTO updateAgg, @PathVariable String uuid){
//        return ResponseEntity.status(HttpStatus.OK).body(carAggregateService.updateAggregate(updateAgg, uuid));
//    }
//
    @DeleteMapping(
            value = "/car/{carUUID}",
            produces = "application/json"
    )
    public Mono<Void> delete(@PathVariable String carUUID){
        return carAggregateService.deleteCarAggregate(carUUID);
    }
}
