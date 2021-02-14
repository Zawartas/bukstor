package com.sztukakodu.bukstor;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase.CreateBookCommand;
import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase.UpdateBookResponse;
import com.sztukakodu.bukstor.catalog.domain.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
        initData();
        findByTitle();
//        findByAuthor();
        findAndUpdate();
        findByTitle();
    }

    private void initData() {
        catalog.addBook(new CreateBookCommand("Henryk Potter cz.1", "J.K.Rawling", 2001));
        catalog.addBook(new CreateBookCommand("Harry i komnata tajemnic", "J.K.Rawling", 2002));
        catalog.addBook(new CreateBookCommand("On i Harry Potter cz.3", "Piekara", 2003));
        catalog.addBook(new CreateBookCommand("Wiedzmin", "Andrzej Sapkowski", 2004));
    }

    private void findByAuthor() {
        System.out.println("----by author----");
        List<Book> books = catalog.findByAuthor("Sapk");
        books.forEach(System.out::println);
    }

    private void findByTitle() {
        System.out.println("----by title----");
        List<Book> books = catalog.findByTitle(title);
        books.forEach(System.out::println);
    }

    private void findAndUpdate() {
        catalog.findOneByTitleAndAuthor("Wiedzmin", "Sapkowski")
                .ifPresent(book -> {
                    UpdateBookCommand update = UpdateBookCommand
                            .builder()
                            .id(book.getId())
                            .title("Wiedzmin opowiadania")
                            .build();
                    final UpdateBookResponse response = catalog.updateBook(update);
                    if (response == UpdateBookResponse.SUCCESS) {
                        System.out.println("Sukces");
                    } else {
                        System.out.println(response.getErrors().toString());
                    }
                });
    }
}

