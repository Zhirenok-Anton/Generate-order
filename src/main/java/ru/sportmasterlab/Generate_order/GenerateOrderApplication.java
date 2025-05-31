package ru.sportmasterlab.Generate_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"application.properties"})
public class GenerateOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenerateOrderApplication.class, args);
	}
}
