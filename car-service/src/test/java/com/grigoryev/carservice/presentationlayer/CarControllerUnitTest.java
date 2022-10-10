package com.grigoryev.carservice.presentationlayer;

import com.grigoryev.carservice.deliverancelayer.Car;
import com.grigoryev.carservice.servicelayer.CarDTO;
import com.grigoryev.carservice.servicelayer.CarService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static reactor.core.publisher.Mono.just;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


@WebFluxTest(controllers = CarController.class)
class CarControllerUnitTest {

    private CarDTO dto = buildCarDTO();
    private final String CAR_UUID_OK = dto.getCarUUID();

    @Autowired
    private WebTestClient client;

    @MockBean
    CarService carService;

    @Test
    void getAllCars() {

        when(carService.getAll()).thenReturn(Flux.just(dto));

        client.get()
                .uri("/car")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].modelName").isEqualTo(dto.getModelName())
                .jsonPath("[0].type").isEqualTo(dto.getType())
                .jsonPath("[0].carUUID").isEqualTo(dto.getCarUUID());

        Mockito.verify(carService, times(1)).getAll();


    }

    @Test
    void getCarByCarUUID() {

        when(carService.getCarByCarUUID(anyString())).thenReturn(Mono.just(dto));

        client.get()
                .uri("/car/" + CAR_UUID_OK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.modelName").isEqualTo(dto.getModelName())
                .jsonPath("$.type").isEqualTo(dto.getType())
                .jsonPath("$.carUUID").isEqualTo(dto.getCarUUID());

        Mockito.verify(carService, times(1)).getCarByCarUUID(CAR_UUID_OK);
    }

    @Test
    void insertCar() {  // Broken

        CarDTO newDTO = buildSpecial();

        Mono<CarDTO> monoDTO =  Mono.just(newDTO);

        when(carService.insertCar(monoDTO)).thenReturn(monoDTO);


        client.post()
                .uri("/car")
                .body(monoDTO, CarDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();



        Mockito.verify(carService, times(1)).insertCar(any(Mono.class));
    }

    @Test
    void updateCar() {

        CarDTO newDTO = buildSpecial();

        Mono<CarDTO> monoDTO =  Mono.just(newDTO);

        when(carService.updateCar(CAR_UUID_OK, monoDTO).thenReturn(CAR_UUID_OK).thenReturn(monoDTO));

        CarDTO carDTO2 = buildCarDTO();

        carDTO2.setModelName("Ninja 650");

        client.put()
                .uri("/car/" + CAR_UUID_OK)
                .body(just(carDTO2), CarDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();

        Mockito.verify(carService, times(1)).updateCar(anyString(), any(Mono.class));


    }

    @Test
    void deleteCarByCarUUID() {

        when(carService.getCarByCarUUID(anyString())).thenReturn(Mono.just(dto));

        client.delete()
                .uri("/car/" + CAR_UUID_OK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();

        Mockito.verify(carService, times(1)).deleteCar(CAR_UUID_OK);
    }

    private CarDTO buildCarDTO(){
        return CarDTO.builder().carUUID("CarUUID").modelName("Ninja 400").type("Motorcycle").weight(100).length(7).height(3).basePrice(6500).build();
    }

    private CarDTO buildSpecial(){
        return CarDTO.builder().carUUID("CarUUID2").modelName("Ninja 650").type("Motorcycle").weight(110).length(7).height(3).basePrice(10000).build();
    }
}