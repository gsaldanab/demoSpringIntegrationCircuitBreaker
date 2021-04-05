package com.geo.demospringintegrationcircuitbreaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:integration3.xml")
public class DemoSpringIntegrationCircuitBreakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringIntegrationCircuitBreakerApplication.class, args);
	}

}
