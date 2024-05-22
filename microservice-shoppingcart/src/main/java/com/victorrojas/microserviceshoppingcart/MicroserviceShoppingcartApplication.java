package com.victorrojas.microserviceshoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MicroserviceShoppingcartApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceShoppingcartApplication.class, args);
	}

}
