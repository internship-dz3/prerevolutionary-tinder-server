package com.liga.internship.client.server;

import com.liga.internship.client.server.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SwaggerConfiguration.class)
public class PrerevolutionaryTinderServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(PrerevolutionaryTinderServerApplication.class, args);
	}

}
