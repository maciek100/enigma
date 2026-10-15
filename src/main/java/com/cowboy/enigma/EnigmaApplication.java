package com.cowboy.enigma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point. Lives in the root package so Spring scans every
 * sub-package; the core (com.cowboy.enigma.core) has no Spring imports.
 */
@SpringBootApplication
public class EnigmaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnigmaApplication.class, args);
    }
}
