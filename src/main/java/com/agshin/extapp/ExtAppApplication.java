package com.agshin.extapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class ExtAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExtAppApplication.class, args);
    }
}
