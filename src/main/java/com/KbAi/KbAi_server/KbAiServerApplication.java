package com.KbAi.KbAi_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KbAiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KbAiServerApplication.class, args);
	}

}
