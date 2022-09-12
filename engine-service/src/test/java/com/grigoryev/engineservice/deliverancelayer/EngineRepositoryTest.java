package com.grigoryev.engineservice.deliverancelayer;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class EngineRepositoryTest {

    @Autowired
    private EngineRepository repo;

    // 2022-09-12 00:05:18.510  INFO 4420 --- [    Test worker] org.mongodb.driver.cluster               : No server chosen by com.mongodb.reactivestreams.client.internal.ClientSessionHelper$$Lambda$1176/0x00000008011d0e40@38e00b47 from cluster description ClusterDescription{type=UNKNOWN, connectionMode=SINGLE, serverDescriptions=[ServerDescription{address=localhost:27017, type=UNKNOWN, state=CONNECTING, exception={com.mongodb.MongoSocketOpenException: Exception opening socket}, caused by {io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: no further information: localhost/[0:0:0:0:0:0:0:1]:27017}, caused by {java.net.ConnectException: Connection refused: no further information}}]}. Waiting for 30000 ms before timing out
    // :)

    @Test
    void shouldSaveOneEngine() {

        Publisher<Engine> setup = repo.deleteAll().thenMany(repo.save(buildEngine()));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findEnginesByCarUUID() {
    }

    @Test
    void deleteEngineByEngineUUID() {
    }

    @Test
    void deleteEngineByCarUUID() {
    }

    private Engine buildEngine(){
        return Engine.builder().id("Id").engineUUID("EngineUUID")
                .carUUID("CarUUID").name("L5P").cylinders(8).fuelType("Diesel").price(10000).build();
    }
}