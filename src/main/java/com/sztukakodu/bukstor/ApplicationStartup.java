package com.sztukakodu.bukstor;

import com.sztukakodu.bukstor.catalog.application.CatalogController;
import com.sztukakodu.bukstor.catalog.domain.Book;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogController controller;
    private final String title;

    public ApplicationStartup(
            CatalogController controller,
            @Value("${title}") String title) {
        this.controller = controller;
        this.title = title;
    }

    @Override
    public void run(String... args) {
        System.out.println("----by title----");
        List<Book> books = this.controller.findByTitle(title);
        books.forEach(System.out::println);

        System.out.println("----by author----");
        books = this.controller.findByAuthor("Sien");
        books.forEach(System.out::println);
    }
}

