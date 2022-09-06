package com.grigoryev.carservice.presentationlayer;

import com.grigoryev.carservice.servicelayer.CarDTO;
import com.grigoryev.carservice.servicelayer.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("car")
public class CarController {

    @Autowired
    private CarService carService;


    @GetMapping()
    public Flux<CarDTO> getAllCars(){
        return carService.getAll();
    }


    @GetMapping("/{carUUID}")
    public Mono<CarDTO> getCarByCarUUID(@PathVariable String carUUID){
        return carService.getCarByCarUUID(carUUID);
    }


    @PostMapping()
    public Mono<CarDTO> insertCar(@RequestBody Mono<CarDTO> carDTOMono){
        return carService.insertCar(carDTOMono);
    }


    @PutMapping("/{carUUID}")
    public Mono<CarDTO> updateCar(@PathVariable String carUUID, @RequestBody Mono<CarDTO> carDTOMono){
        return carService.updateCar(carUUID, carDTOMono);
    }


    @DeleteMapping("/{carUUID}")
    public Mono<Void> deleteCarByCarUUID(@PathVariable String carUUID){
        return carService.deleteCar(carUUID);
    }
}