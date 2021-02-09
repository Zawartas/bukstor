package com.sztukakodu.bukstor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BukstorWwwSzopApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BukstorWwwSzopApplication.class, args);
	}

	private CatalogService catalog;

	public BukstorWwwSzopApplication(CatalogService catalog) {
		this.catalog = catalog;
	}

	@Override
	public void run(String... args) throws Exception {
		List<Book> books = catalog.findByTitle("Pan");
		books.forEach(System.out::println);
	}
}
