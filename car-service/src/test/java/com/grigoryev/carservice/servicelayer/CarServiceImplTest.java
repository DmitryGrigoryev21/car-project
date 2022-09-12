package com.grigoryev.carservice.servicelayer;

import com.grigoryev.carservice.deliverancelayer.Car;
import com.grigoryev.carservice.deliverancelayer.CarRepository;
import com.grigoryev.carservice.util.EntityDTOUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
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
    }

    @Test
    void insertCar() {

        Car carEntity = buildCar();

        EntityDTOUtil dtoObj = new EntityDTOUtil();

        carService.insertCar(Mono.just(dtoObj.toDTO(carEntity)));

        assertNull(repo.findCarByCarUUID(carEntity.getCarUUID()));  // Not working for some reason


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
        String uuid = carEntity.getCarUUID();
        repo.save(carEntity);
        Car updatedCar = Car.builder().id("Id").carUUID(uuid).modelName("Ninja 650").type("Motorcycle").weight(150).length(7).height(3).basePrice(10500).build();

        EntityDTOUtil dtoObj = new EntityDTOUtil();

        when(repo.findCarByCarUUID(anyString())).thenReturn((Mono.just(carEntity)));

        carService.updateCar(uuid, Mono.just(dtoObj.toDTO(updatedCar)));

        assertNotEquals(Mono.just(carEntity), repo.findCarByCarUUID(updatedCar.getCarUUID()));

    }

    @Test
    void deleteCar() {

        Car carEntity = buildCar();

        repo.save(carEntity);

        carService.deleteCar(carEntity.getCarUUID());

        assertNull(repo.findCarByCarUUID(carEntity.getCarUUID()));
    }

    private Car buildCar(){
        return Car.builder().id("Id").carUUID("CarUUID").modelName("Ninja 400").type("Motorcycle").weight(100).length(7).height(3).basePrice(6500).build();
    }
}