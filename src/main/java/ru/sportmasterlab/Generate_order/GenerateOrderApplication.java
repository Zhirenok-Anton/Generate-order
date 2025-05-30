package ru.sportmasterlab.Generate_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.sportmasterlab.Generate_order.services.OrderService;
import ru.sportmasterlab.Generate_order.services.OrderServiceImpl;


@SpringBootApplication
public class GenerateOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenerateOrderApplication.class, args);
	}
}
