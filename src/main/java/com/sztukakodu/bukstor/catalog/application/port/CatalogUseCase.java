package com.sztukakodu.bukstor.catalog.application.port;

import com.sztukakodu.bukstor.catalog.domain.Book;

import java.util.List;
import java.util.Optional;

public interface CatalogUseCase {
    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    Optional<Book> findOneByTitleAndAuthor(String title, String author);

    void addBook();

    void removeBookById(Long id);

    void updateBook();
}
