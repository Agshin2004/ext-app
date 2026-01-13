package com.agshin.extapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExtAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExtAppApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("TODO: Implement printing out expenses.");
    }
}
