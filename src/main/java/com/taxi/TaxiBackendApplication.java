package com.taxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class TaxiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaxiBackendApplication.class, args);
	}

}
