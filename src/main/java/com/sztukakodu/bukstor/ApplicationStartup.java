package com.sztukakodu.bukstor;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.catalog.domain.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final String title;

    public ApplicationStartup(
            CatalogUseCase catalog,
            @Value("${title}") String title) {
        this.catalog = catalog;
        this.title = title;
    }

    @Override
    public void run(String... args) {
        System.out.println("----by title----");
        List<Book> books = catalog.findByTitle(title);
        books.forEach(System.out::println);

        System.out.println("----by author----");
        books = catalog.findByAuthor("Sien");
        books.forEach(System.out::println);
    }
}

