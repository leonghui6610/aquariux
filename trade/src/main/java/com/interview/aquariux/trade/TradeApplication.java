package com.interview.aquariux.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ComponentScan(basePackages = {"com.interview.aquariux.trade.service", "com.interview.aquariux.trade.controller"})
@EntityScan(basePackageClasses = Jsr310Converters.class, basePackages = {"com.interview.aquariux.trade.entities"})
@EnableJpaRepositories(basePackages = {"com.interview.aquariux.trade.entities"})
@SpringBootApplication
public class TradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class, args);
    }

}


/*
http://localhost:8080/h2-console/
jdbc URL: jdbc:h2:file:./dataTrade
username: interview
password: interview
 */