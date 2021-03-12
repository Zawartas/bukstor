package com.sztukakodu.bukstor.catalog.application;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.catalog.db.AuthorJpaRepository;
import com.sztukakodu.bukstor.catalog.db.BookJpaRepository;
import com.sztukakodu.bukstor.catalog.domain.Author;
import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.uploads.application.ports.UploadUseCase;
import com.sztukakodu.bukstor.uploads.application.ports.UploadUseCase.SaveUploadCommand;
import com.sztukakodu.bukstor.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {

    private final BookJpaRepository bookRepository;
    private final AuthorJpaRepository authorRepository;
    private final UploadUseCase upload;

//    public CatalogService(@Qualifier("memoryCatalogRepository") CatalogRepository repository,
//                          UploadUseCase upload) {
//        this.repository = repository;
//        this.upload = upload;
//    }

    @Override
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitleStartsWithIgnoreCase(title);
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return bookRepository.findDistinctFirstByTitleStartsWithIgnoreCase(title);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book addBook(CreateBookCommand command) {
        Book book = toBook(command);
        return bookRepository.save(book);
    }

    private Book toBook(CreateBookCommand command) {
        Book book = new Book(command.getTitle(), command.getYear(), command.getPrice());
        var authors = fetchAuthorsByIds(command.getAuthors());
        book.setAuthors(authors);
        return book;
    }

    private Set<Author> fetchAuthorsByIds(Set<Long> authors) {
        return authors
                .stream()
                .map(authorId ->
                        authorRepository.findById(authorId)
                                .orElseThrow(() -> new IllegalArgumentException("author ble")))
                .collect(Collectors.toSet());
    }

    @Override
    public void removeBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return bookRepository.findById(command.getId())
                .map(book -> {
                    final Book updatedBook = updateFields(command, book);
                    bookRepository.save(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() ->
                        new UpdateBookResponse(false, Arrays.asList("Book id not found: " + command.getId())));
    }

    private Book updateFields(UpdateBookCommand command, Book book) {

        if (command.getTitle() != null) {
            book.setTitle(command.getTitle());
        }
        if (command.getAuthors() != null && command.getAuthors().size() > 0) {
            book.setAuthors(fetchAuthorsByIds(command.getAuthors()));
        }
        if (command.getYear() != null) {
            book.setYear(command.getYear());
        }
        if (command.getPrice() != null) {
            book.setPrice(command.getPrice());
        }
        return book;
    }

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        int length = command.getFile().length;
        System.out.println("Received cover command: " + command.getFilename() + ", size: " + length);
        bookRepository.findById(command.getId())
                .ifPresent(book -> {
                    final Upload upload = this.upload.save(new SaveUploadCommand(command.getFilename(),
                            command.getFile(), command.getContentType()));
                    book.setCoverId(upload.getId());
                    bookRepository.save(book);
                });
    }

    @Override
    public void removeBookCover(Long id) {
        bookRepository.findById(id)
                .ifPresent(book -> {
                    if (book.getCoverId() != null) {
                        upload.removeById(book.getCoverId());
                        book.setCoverId(null);
                        bookRepository.save(book);
                    }
                });
    }
}
