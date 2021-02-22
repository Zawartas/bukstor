package com.sztukakodu.bukstor.catalog.infrastructure;

import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.catalog.domain.CatalogRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class SchoolCatalogRepository implements CatalogRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(Long id) {
        //
    }
}
