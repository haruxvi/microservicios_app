package com.microservice.game_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceGameApplication.class, args);
	}

}
