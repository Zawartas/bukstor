package com.sztukakodu.bukstor;

import com.sztukakodu.bukstor.catalog.application.CatalogController;
import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.catalog.domain.CatalogService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BukstorWwwSzopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BukstorWwwSzopApplication.class, args);
	}

}
