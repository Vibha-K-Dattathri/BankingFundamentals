package com.ofss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
public class HealthMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthMsApplication.class, args);
		System.out.println("Health MS running successfully...");
	}

}
