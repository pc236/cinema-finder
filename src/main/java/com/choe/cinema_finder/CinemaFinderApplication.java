package com.choe.cinema_finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.choe.cinema_finder")
@EnableJpaRepositories("com.choe.cinema_finder.repository")
@EntityScan("com.choe.cinema_finder.entity")
public class CinemaFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaFinderApplication.class, args);
    }
}