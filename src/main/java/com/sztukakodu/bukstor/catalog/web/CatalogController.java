package com.sztukakodu.bukstor.catalog.web;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.catalog.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.*;

import static com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase.*;

@RestController
@RequestMapping("/catalog")
@AllArgsConstructor
public class CatalogController {

    private final CatalogUseCase catalog;

    @GetMapping
    public List<Book> getAll(@RequestParam Optional<String> title,
                             @RequestParam Optional<String> author) {
        if (title.isPresent() && author.isPresent()) {
            return catalog.findByTitleAndAuthor(title.get(), author.get());
        }
        if (title.isPresent()) {
            return catalog.findByTitle(title.get());
        }
        if (author.isPresent()) {
            return catalog.findByAuthor(author.get());
        }
        return catalog.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        if (id.equals(42L)) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I am teapot, sorry.");
        }
        return catalog
                .findById(id)
                .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addBook(@Valid @RequestBody RestCreateBookCommand command) {
        Book book = catalog.addBook(command.toCreateCommand());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/" + book.getId().toString()).build().toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}/cover", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addBookCover(@PathVariable Long id, @RequestParam("file") MultipartFile file)
            throws IOException {
        System.out.println("Got file: " + file.getOriginalFilename());
        catalog.updateBookCover(
                new UpdateBookCoverCommand(
                        id, file.getBytes(), file.getContentType(), file.getOriginalFilename()));
    }

    @DeleteMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookCover(@PathVariable Long id) {
        catalog.removeBookCover(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBook(@PathVariable Long id, @Valid @RequestBody RestUpdateBookCommand command) {
        UpdateBookResponse response = catalog.updateBook(command.toUpdateCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        catalog.removeBookById(id);
    }

    @Data
    private static class RestCreateBookCommand {

        @NotBlank(message = "please provide a title")
        private String title;

        @NotEmpty
        private Set<Long> authors;

        @NotNull
        @Positive
        private Integer year;

        @NotNull
        @PositiveOrZero
        private BigDecimal price;

        CreateBookCommand toCreateCommand() {
            return new CreateBookCommand(title, authors, year, price);
        }
    }

    @Data
    private static class RestUpdateBookCommand {

        @Pattern(regexp = "^[a-zA-Z]+( [a-zA-Z]+)*$")
        private String title;
        
        private Set<Long> authors;

        @Positive
        private Integer year;

        @PositiveOrZero
        private BigDecimal price;

        UpdateBookCommand toUpdateCommand(Long id) {
            return new UpdateBookCommand(id, title, authors, year, price);
        }
    }


}
