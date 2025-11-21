package com.microservice.auth_service.microservice_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceAuthServiceApplication.class, args);
	}

}
