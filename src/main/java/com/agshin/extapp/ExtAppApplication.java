package com.agshin.extapp;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class ExtAppApplication implements CommandLineRunner {

    private final DataSource dataSource;

    public ExtAppApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase l = new SpringLiquibase();
        l.setChangeLog("classpath:/db/changelog/db.changelog-master.yaml");
        l.setDataSource(dataSource);
        return l;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExtAppApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("TODO: Implement printing out expenses.");
    }
}
