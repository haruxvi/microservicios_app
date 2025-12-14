package com.microservice.auth_service.microservice_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.microservice.auth_service.microservice_auth_service.model")
@EnableJpaRepositories("com.microservice.auth_service.microservice_auth_service.repository")
@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceAuthServiceApplication.class, args);
	}

}
