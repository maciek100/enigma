package com.cowboy.enigma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point. Lives in the api package so the core
 * (com.cowboy.enigma) stays a plain Java library with no Spring dependency.
 */
@SpringBootApplication
public class EnigmaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnigmaApplication.class, args);
    }
}
