package com.usermanagement;

import com.usermanagement.config.*;

import com.microsoft.applicationinsights.extensibility.TelemetryProcessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserService {

	public static void main(String[] args) {
		SpringApplication.run(UserService.class, args);
	}

	@Bean
	public TelemetryProcessor requestFilter() {
		return new RequestFilter();
	}

}
