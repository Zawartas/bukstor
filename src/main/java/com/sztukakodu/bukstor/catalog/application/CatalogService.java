package com.sztukakodu.bukstor.catalog.application;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.catalog.domain.CatalogRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class CatalogService implements CatalogUseCase {

    private final CatalogRepository repository;

    public CatalogService(@Qualifier("memoryCatalogRepository") CatalogRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Book> findByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().startsWith(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().startsWith(title))
                .findFirst();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().contains(author))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return repository.findAll().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public Book addBook(CreateBookCommand command) {
        Book book = new Book(command.getTitle(), command.getAuthor(), command.getYear(), command.getPrice());
        return repository.save(book);
    }

    @Override
    public void removeBookById(Long id) {
        repository.removeById(id);
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return repository.findById(command.getId())
                .map(book -> {
                    command.updateFields(book);
                    repository.save(book);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() ->
                        new UpdateBookResponse(false, Arrays.asList("Book id not found: " + command.getId())));
    }
}
