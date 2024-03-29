package com.sztukakodu.bukstor.catalog.application.port;

import com.sztukakodu.bukstor.catalog.domain.Author;
import com.sztukakodu.bukstor.catalog.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CatalogUseCase {

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    Optional<Book> findOneByTitle(String title);

    List<Book> findByTitleAndAuthor(String title, String author);

    Optional<Book> findById(Long id);

    Book addBook(CreateBookCommand command);

    void removeBookById(Long id);

    UpdateBookResponse updateBook(UpdateBookCommand command);

    void updateBookCover(UpdateBookCoverCommand command);

    void removeBookCover(Long id);

    @Value
    class CreateBookCommand {
        String title;
        Set<Long> authors;
        Integer year;
        BigDecimal price;
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateBookCommand {
        Long id;
        String title;
        Set<Long> authors;
        Integer year;
        BigDecimal price;
    }

    @Value
    class UpdateBookResponse {
        public static UpdateBookResponse SUCCESS = new UpdateBookResponse(true, Collections.emptyList());
        boolean success;
        List<String> errors;
    }

    @Value
    class UpdateBookCoverCommand {
        Long id;
        byte[] file;
        String contentType;
        String filename;
    }
}
