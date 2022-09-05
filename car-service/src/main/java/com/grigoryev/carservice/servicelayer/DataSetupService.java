package com.grigoryev.carservice.servicelayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DataSetupService implements CommandLineRunner {

    @Autowired
    private CarService carService;

    @Override
    public void run(String... args) throws Exception {

        CarDTO c1 = new CarDTO("669a7e3e-ed97-44d4-90a1-dd73c1141079", "Viper", "coupe", 2000, 7, 3, 30000);
        CarDTO c2 = new CarDTO("dd6643b6-a869-4885-a941-619a48058938", "Cobra", "sedan", 2500, 8, 3, 35000);

        Flux.just(c1, c2)
                .flatMap(p -> carService.insertCar(Mono.just(p))
                        .log(p.toString()))
                .subscribe();
    }
}