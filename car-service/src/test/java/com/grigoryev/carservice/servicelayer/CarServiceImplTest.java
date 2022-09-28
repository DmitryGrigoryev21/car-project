package com.grigoryev.carservice.servicelayer;

import com.grigoryev.carservice.deliverancelayer.Car;
import com.grigoryev.carservice.deliverancelayer.CarRepository;
import com.grigoryev.carservice.util.EntityDTOUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 27017"})
@AutoConfigureWebTestClient
class CarServiceImplTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private CarService carService;

    @MockBean
    private CarRepository repo;


    @Test
    void getAll() {

        Car carEntity = buildCar();

        when(repo.findAll()).thenReturn(Flux.just(carEntity));

        Flux<CarDTO> carDTOFlux = carService.getAll();

        StepVerifier.create(carDTOFlux)
                .consumeNextWith(foundCar -> {
                    assertNotNull(foundCar);
                })
                .verifyComplete();
    }

    @Test
    void insertCar() {

      Car carEntity = buildCar();

      Mono<Car> carMono = Mono.just(carEntity);
      CarDTO carDTO = buildCarDTO();

      when(repo.insert(any(Car.class))).thenReturn(carMono);

      Mono<CarDTO> returnedCar = carService.insertCar(Mono.just(carDTO));

      StepVerifier.create(returnedCar)
              .consumeNextWith(monoDTO -> {
                  assertEquals(carEntity.getModelName(), monoDTO.getModelName());
                  assertEquals(carEntity.getCarUUID(), monoDTO.getCarUUID());
              })
              .verifyComplete();

    }

    @Test
    void getCarByCarUUID() {

        Car carEntity = buildCar();

        String CAR_UUID = carEntity.getCarUUID();

        when(repo.findCarByCarUUID(anyString())).thenReturn(Mono.just(carEntity));

        Mono<CarDTO> carDTOMono = carService.getCarByCarUUID(CAR_UUID);

        StepVerifier.create(carDTOMono)
                .consumeNextWith(foundCar -> {
                    assertEquals(carEntity.getBasePrice(), foundCar.getBasePrice());
                    assertEquals(carEntity.getHeight(), foundCar.getHeight());
                    assertEquals(carEntity.getLength(), foundCar.getLength());
                })
                .verifyComplete();
    }

    @Test
    void updateCar() {

        Car carEntity = buildCar();
        String CAR_UUID = carEntity.getCarUUID();
        CarDTO dto = buildCarDTO();
        dto.setModelName("Ninja 650");

        Car updatedCarEntity = new Car();
        BeanUtils.copyProperties(carEntity, updatedCarEntity);                              // This part here
        updatedCarEntity.setModelName(dto.getModelName());
        when(repo.findCarByCarUUID(anyString())).thenReturn(Mono.just(carEntity));
        when(repo.save(any(Car.class))).thenReturn(Mono.just(updatedCarEntity));

        Mono<CarDTO> carDTOMono = carService.updateCar(CAR_UUID, Mono.just(dto));           // This is what gets step verified

        StepVerifier.create(carDTOMono)
                .consumeNextWith(foundCar -> {
                    assertNotEquals(carEntity.getModelName(), foundCar.getModelName());
                })
                .verifyComplete();

    }

    @Test
    void deleteCar() {

        Car carEntity = buildCar();

        when(repo.deleteCarByCarUUID(anyString())).thenReturn(Mono.empty());

        Mono<Void> deletedObj = carService.deleteCar(carEntity.getCarUUID());

        StepVerifier.create(deletedObj)
                .expectNextCount(0)
                .verifyComplete();
    }

    private Car buildCar(){
        return Car.builder().id("Id").carUUID("CarUUID").modelName("Ninja 400").type("Motorcycle").weight(100).length(7).height(3).basePrice(6500).build();
    }


    private CarDTO buildCarDTO(){
        return CarDTO.builder().carUUID("CarUUID").modelName("Ninja 400").type("Motorcycle").weight(100).length(7).height(3).basePrice(6500).build();
    }
}