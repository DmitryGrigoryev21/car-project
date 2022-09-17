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
    void getAllEngines() {
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

        Mockito.verify(engineService, times(1)).getEngineByEngineUUID(ENGINE_UUID_OK);
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
    }

    @Test
    void insertEngine() {
    }

    @Test
    void updateEngine() {
    }

    @Test
    void deleteEngineByEngineUUID() {
    }

    @Test
    void deleteEngineByCarUUID() {
    }

    private EngineDTO buildEngineDTO(){
        return EngineDTO.builder().engineUUID("EngineUUID")
                .carUUID("CarUUID").name("L5P").cylinders(8).fuelType("Diesel").price(10000).build();
    }
}