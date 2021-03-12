package com.sztukakodu.bukstor.catalog.web;

import com.sztukakodu.bukstor.catalog.application.port.AuthorUseCase;
import com.sztukakodu.bukstor.catalog.domain.Author;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authors")
@AllArgsConstructor
public class AuthorsController {

    public final AuthorUseCase authors;

    @GetMapping
    public List<Author> findAll() {
        return authors.findAll();
    }
}
