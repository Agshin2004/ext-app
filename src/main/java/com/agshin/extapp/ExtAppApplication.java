package com.agshin.extapp;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class ExtAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExtAppApplication.class, args);
    }
}
