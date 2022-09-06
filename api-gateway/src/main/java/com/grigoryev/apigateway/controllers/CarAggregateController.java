package com.grigoryev.apigateway.controllers;

import com.grigoryev.apigateway.services.CarAggregateDTO;
import com.grigoryev.apigateway.services.CarAggregateService;
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
    public Flux<ResponseEntity<CarAggregateDTO>> getAllCarAggregates(){
        return carAggregateService.getAllCarAggregates()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(
            value = "/car/{carUUID}",
            produces = "application/json"
    )
    public Mono<ResponseEntity<CarAggregateDTO>> getAllCarByCarUUID(@PathVariable String carUUID){
        return carAggregateService.getCarAggregate(carUUID)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

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
//    @DeleteMapping(
//            value = "/car/{caruuid}"
//    )
//    public void deleteAggregate(@PathVariable String uuid){
//
//        carAggregateService.delete(uuid);
//    }
}
