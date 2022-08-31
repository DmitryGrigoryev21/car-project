package com.grigoryev.engineservice.presentationlayer;

import com.grigoryev.engineservice.servicelayer.EngineDTO;
import com.grigoryev.engineservice.servicelayer.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("engine")
public class EngineController {

    @Autowired
    private EngineService engineService;


    @GetMapping()
    public Flux<EngineDTO> getAllEngines(){
        return engineService.getAll();
    }


    @GetMapping("/{engineUUID}")
    public Mono<ResponseEntity<EngineDTO>> getEngineByEngineUUID(@PathVariable String engineUUID){
        return engineService.getEngineByEngineUUID(engineUUID)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping("/car/{carUUID}")
    public Flux<ResponseEntity<EngineDTO>> getEngineByCarUUID(@PathVariable String carUUID){
        return engineService.getEnginesByCarUUID(carUUID)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PostMapping()
    public Mono<EngineDTO> insertEngine(@RequestBody Mono<EngineDTO> engineDTOMono){
        return engineService.insertEngine(engineDTOMono);
    }


    @PutMapping("/{engineUUID}")
    public Mono<EngineDTO> updateEngine(@PathVariable String engineUUID, @RequestBody Mono<EngineDTO> engineDTOMono){
        return engineService.updateEngine(engineUUID, engineDTOMono);
    }


    @DeleteMapping("/{engineUUID}")
    public Mono<Void> deleteEngineByEngineUUID(@PathVariable String engineUUID){
        return engineService.deleteEngine(engineUUID);
    }
}