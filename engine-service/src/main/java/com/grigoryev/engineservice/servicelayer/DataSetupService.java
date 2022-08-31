package com.grigoryev.engineservice.servicelayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DataSetupService implements CommandLineRunner {

    @Autowired
    private EngineService engineService;

    @Override
    public void run(String... args) throws Exception {

        EngineDTO e1 = new EngineDTO("669a7e3e-ed97-44d4-90a1-dd73c1141079", "LR4", "gasoline", 6,5100);
        EngineDTO e2 = new EngineDTO("669a7e3e-ed97-44d4-90a1-dd73c1141079", "LS1", "gasoline", 8,5500);
        EngineDTO e3 = new EngineDTO("669a7e3e-ed97-44d4-90a1-dd73c1141079", "LQ9", "gasoline", 8,6200);
        EngineDTO e4 = new EngineDTO("dd6643b6-a869-4885-a941-619a48058938", "LR4", "gasoline", 6,5100);
        EngineDTO e5 = new EngineDTO("dd6643b6-a869-4885-a941-619a48058938", "L76", "diesel", 6,4800);

        Flux.just(e1, e2, e3, e4, e5)
                .flatMap(p -> engineService.insertEngine(Mono.just(p))
                        .log(p.toString()))
                .subscribe();
    }
}