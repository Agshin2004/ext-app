package com.agshin.extapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
public class ExtAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExtAppApplication.class, args);
    }
}
