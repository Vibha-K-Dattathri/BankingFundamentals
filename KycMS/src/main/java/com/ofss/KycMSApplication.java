package com.ofss;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableDiscoveryClient
@SpringBootApplication
public class KycMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(KycMSApplication.class, args);
		System.out.println("KycMS running successfuly...");
	}

}

