package com.sztukakodu.bukstor.catalog.domain;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Deprecated
public interface CatalogRepository {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    void removeById(Long id);
}
