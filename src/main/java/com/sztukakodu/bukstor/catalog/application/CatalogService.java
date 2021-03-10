package com.sztukakodu.bukstor.catalog.application;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.catalog.db.BookJpaRepository;
import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.catalog.domain.CatalogRepository;
import com.sztukakodu.bukstor.uploads.application.ports.UploadUseCase;
import com.sztukakodu.bukstor.uploads.application.ports.UploadUseCase.SaveUploadCommand;
import com.sztukakodu.bukstor.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {

//    private final CatalogRepository repository;
    private final BookJpaRepository repository;
    private final UploadUseCase upload;

//    public CatalogService(@Qualifier("memoryCatalogRepository") CatalogRepository repository,
//                          UploadUseCase upload) {
//        this.repository = repository;
//        this.upload = upload;
//    }

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
//        repository.removeById(id);
        repository.deleteById(id);
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

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        int length = command.getFile().length;
        System.out.println("Received cover command: " + command.getFilename() + ", size: " + length);
        repository.findById(command.getId())
                .ifPresent(book -> {
                    final Upload upload = this.upload.save(new SaveUploadCommand(command.getFilename(),
                            command.getFile(), command.getContentType()));
                    book.setCoverId(upload.getId());
                    repository.save(book);
                });
    }

    @Override
    public void removeBookCover(Long id) {
        repository.findById(id)
                .ifPresent(book -> {
                    if (book.getCoverId() != null) {
                        upload.removeById(book.getCoverId());
                        book.setCoverId(null);
                        repository.save(book);
                    }
                });
    }
}
