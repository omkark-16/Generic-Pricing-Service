package com.pricingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.pricingservice")
@EntityScan(basePackages = "com.pricingservice.entities")
@EnableJpaRepositories(basePackages = "com.pricingservice.repository")
public class GenericPricingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenericPricingServiceApplication.class, args);
	}

}
