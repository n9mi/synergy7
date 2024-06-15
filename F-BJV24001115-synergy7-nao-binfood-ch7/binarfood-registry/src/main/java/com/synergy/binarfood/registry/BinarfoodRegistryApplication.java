package com.synergy.binarfood.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BinarfoodRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BinarfoodRegistryApplication.class, args);
	}

}
