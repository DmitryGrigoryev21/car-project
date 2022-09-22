package com.grigoryev.engineservice.presentationlayer;

import com.grigoryev.engineservice.deliverancelayer.Engine;
import com.grigoryev.engineservice.servicelayer.EngineDTO;
import com.grigoryev.engineservice.servicelayer.EngineService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static reactor.core.publisher.Mono.just;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@WebFluxTest(controllers = EngineController.class)
class EngineControllerUnitTest {

    private EngineDTO dto = buildEngineDTO();
    private final String ENGINE_UUID_OK = dto.getEngineUUID();
    private final String CAR_UUID_OK = dto.getCarUUID();

    @Autowired
    private WebTestClient client;

    @MockBean
    EngineService engineService;

    @Test
    void getAllEngines() { // Does not work

        when(engineService.getAll()).thenReturn(Flux.just(dto));

        client.get()
                .uri("/engine")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].engineUUID").isEqualTo(dto.getEngineUUID())
                .jsonPath("$[0].name").isEqualTo(dto.getName())
                .jsonPath("$[0].fuelType").isEqualTo(dto.getFuelType())
                .jsonPath("$[0].carUUID").isEqualTo(dto.getCarUUID());

        Mockito.verify(engineService, times(1)).getAll();
    }

    @Test
    void getEngineByEngineUUID() {

        when(engineService.getEngineByEngineUUID(anyString())).thenReturn(Mono.just(dto));

        client.get()
                .uri("/engine/" + ENGINE_UUID_OK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.engineUUID").isEqualTo(dto.getEngineUUID())
                .jsonPath("$.name").isEqualTo(dto.getName())
                .jsonPath("$.fuelType").isEqualTo(dto.getFuelType())
                .jsonPath("$.carUUID").isEqualTo(dto.getCarUUID());

        Mockito.verify(engineService, times(1)).getEngineByEngineUUID(ENGINE_UUID_OK);      // Checks if the method runs 'n' amount of times.
    }

    @Test
    void getEngineByCarUUID() {

        when(engineService.getEnginesByCarUUID(anyString())).thenReturn(Flux.just(dto));

        client.get()
                .uri("/engine/car/" + CAR_UUID_OK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].engineUUID").isEqualTo(dto.getEngineUUID())
                .jsonPath("$[0].name").isEqualTo(dto.getName())
                .jsonPath("$[0].fuelType").isEqualTo(dto.getFuelType())
                .jsonPath("$[0].carUUID").isEqualTo(dto.getCarUUID());

        Mockito.verify(engineService, times(1)).getEnginesByCarUUID(CAR_UUID_OK);
    }

    @Test
    void insertEngine() {

        when(engineService.getEngineByEngineUUID(anyString())).thenReturn(Mono.just(dto));

        client.post()
                .uri("/engine")
                .body(just(dto), EngineDTO.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();

        Mockito.verify(engineService, times(1)).insertEngine(Mono.just(dto));       // This is broken (ask for help in the morning)
    }

    @Test
    void updateEngine() {           // Does not work

        when(engineService.getEngineByEngineUUID(anyString())).thenReturn(Mono.just(dto));
        EngineDTO engineDto2 = buildEngineDTO();
        engineDto2.setName("LB7");

        client.put()
                .uri("/engine/" + ENGINE_UUID_OK)
                .body(just(engineDto2), EngineDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
                // .jsonPath("$.name").isEqualTo(engineDto2.getName())
                // .jsonPath("$.fuelType").isEqualTo(dto.getFuelType());

        Mockito.verify(engineService, times(1)).updateEngine(ENGINE_UUID_OK, Mono.just(dto));


    }

    @Test
    void deleteEngineByEngineUUID() {

        when(engineService.getEngineByEngineUUID(anyString())).thenReturn(Mono.just(dto));

        client.delete()
                .uri("/engine/" + ENGINE_UUID_OK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();

        Mockito.verify(engineService, times(1)).deleteEngine(ENGINE_UUID_OK);
    }

    @Test
    void deleteEngineByCarUUID() {

        when(engineService.getEnginesByCarUUID(anyString())).thenReturn(Flux.just(dto));

        client.delete()
                .uri("/engine/car/" + CAR_UUID_OK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();

        Mockito.verify(engineService, times(1)).deleteEngineByCar(CAR_UUID_OK);


    }

    private EngineDTO buildEngineDTO(){
        return EngineDTO.builder().engineUUID("EngineUUID")
                .carUUID("CarUUID").name("L5P").cylinders(8).fuelType("Diesel").price(10000).build();
    }
}