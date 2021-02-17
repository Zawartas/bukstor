package com.sztukakodu.bukstor.catalog.web;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.catalog.domain.Book;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/catalog")
@AllArgsConstructor
public class CatalogController {

    private final CatalogUseCase catalog;

    @GetMapping
    public List<Book> getAll() {
        return catalog.findAll();
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id) {
        return catalog.findById(id).orElse(null);
    }
}
