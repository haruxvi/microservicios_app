package com.microservice.feedbak_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // 👈 AGREGA ESTA LÍNEA

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceFeedbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceFeedbackApplication.class, args);
    }

}
