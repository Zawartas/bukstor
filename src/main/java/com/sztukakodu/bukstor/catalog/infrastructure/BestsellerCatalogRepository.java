package com.sztukakodu.bukstor.catalog.infrastructure;

import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.catalog.domain.CatalogRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class BestsellerCatalogRepository implements CatalogRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public BestsellerCatalogRepository() {
        storage.put(1L, new Book(1L, "Harry Potter cz.1", "J.K.Rawling", 2001));
        storage.put(2L, new Book(2L, "Harry  cz.2", "J.K.Rawling", 2002));
        storage.put(3L, new Book(3L, "On i Harry Potter cz.3", "Piekara", 2003));
        storage.put(4L, new Book(4L, "Wiedźmin", "Andrzej Sapkowski", 2004));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }
}
