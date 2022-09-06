package com.grigoryev.apigateway.controllers;

import com.grigoryev.apigateway.services.CarAggregateDTO;
import com.grigoryev.apigateway.services.CarAggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class CarAggregateController {

    @Autowired
    private CarAggregateService carAggregateService;

    public CarAggregateController(CarAggregateService carAggregateService){
        this.carAggregateService = carAggregateService;
    }

    @GetMapping(
            value = "/Car",
            produces = "application/json"
    )
    public ResponseEntity<Flux<CarAggregateDTO>> getAllBookAggregates(@PathVariable String carUUID){
        return ResponseEntity.status(HttpStatus.OK).body(carAggregateService.getAllCarAggregates());
    }
}
