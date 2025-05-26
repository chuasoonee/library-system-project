package com.aeonbank.librarysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI customOpenAPI(Environment environment) {
		
		String activeProfile = environment.getActiveProfiles().length > 0 ? environment.getActiveProfiles()[0]
				: "default";

		// Disable Swagger in production
		if ("prod".equals(activeProfile)) {
			return null;
		}

		return new OpenAPI().info(new Info().title("Aeon Bank Library System API (" + activeProfile + ")")
				.description("API Documentation for managing books and loans").version("1.0"));
	}
}