package com.agshin.extapp.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    private final DataSource dataSource;

    public LiquibaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase l = new SpringLiquibase();
        l.setChangeLog("classpath:/db/changelog/db.changelog-master.yaml");
        l.setDataSource(dataSource);
        return l;
    }
}
