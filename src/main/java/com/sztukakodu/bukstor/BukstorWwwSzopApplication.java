package com.sztukakodu.bukstor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BukstorWwwSzopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BukstorWwwSzopApplication.class, args);
	}
}
