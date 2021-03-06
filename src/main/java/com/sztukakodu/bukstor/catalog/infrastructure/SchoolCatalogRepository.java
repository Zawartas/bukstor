package com.sztukakodu.bukstor.catalog.infrastructure;

import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.catalog.domain.CatalogRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class SchoolCatalogRepository implements CatalogRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public SchoolCatalogRepository() {
        storage.put(1L, new Book(1L, "Pan Tadeusz", "Mickiewicz", 2049));
        storage.put(2L, new Book(2L, "1984", "Orwell", 1949));
        storage.put(3L, new Book(3L, "Nowy Wspaniały Swiat", "Huxley", 1971));
        storage.put(4L, new Book(4L, "Pan Wołodyjowski", "Henryk Sienkiewicz", 1891));
        storage.put(5L, new Book(5L, "W pusyni i puszczy", "Sienkiewicz", 1891));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }
}
