package com.sztukakodu.bukstor;

import com.sztukakodu.bukstor.catalog.application.CatalogController;
import com.sztukakodu.bukstor.catalog.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private CatalogController controller;

    @Override
    public void run(String... args) throws Exception {
        List<Book> books = controller.findByTitle("Pan");
        books.forEach(System.out::println);
    }
}
