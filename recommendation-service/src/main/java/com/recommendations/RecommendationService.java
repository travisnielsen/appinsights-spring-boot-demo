package com.recommendations;

import com.recommendations.config.*;

import com.microsoft.applicationinsights.extensibility.TelemetryProcessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecommendationService {
    public static void main(String[] args) {
        SpringApplication.run(RecommendationService.class, args);
    }

    @Bean
	public TelemetryProcessor requestFilter() {
		return new RequestFilter();
	}
}