package com.health.tools.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for User Service.
 * This is a standalone Spring Boot microservice for user management.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.health.tools.userservice.infrastructure.adapter")
@EntityScan(basePackages = "com.health.tools.userservice.infrastructure.entity")
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
