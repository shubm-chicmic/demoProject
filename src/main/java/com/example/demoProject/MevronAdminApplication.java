package com.example.demoProject;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MevronAdminApplication {

	@Value("${stripe.api.key}")
	String stripeApiKey;
	@PostConstruct
	public void setup() {
		// This is your test secret API key.
		Stripe.apiKey = stripeApiKey;

	}
	public static void main(String[] args) {
		SpringApplication.run(MevronAdminApplication.class, args);
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
