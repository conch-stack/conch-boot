package com.nabob.conch.boot.mongosample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class ConchBootMongoSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConchBootMongoSampleApplication.class, args);
    }

}
