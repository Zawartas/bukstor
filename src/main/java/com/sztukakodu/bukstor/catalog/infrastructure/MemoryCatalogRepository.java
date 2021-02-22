package com.sztukakodu.bukstor.catalog.infrastructure;

import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.catalog.domain.CatalogRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
class MemoryCatalogRepository implements CatalogRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_LONG = new AtomicLong(0L);

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(ID_NEXT_LONG.getAndIncrement());
        }
        storage.put(book.getId(), book);
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return findAll().stream()
                .filter(book -> book.getId() == id)
                .findFirst();
    }

    @Override
    public void removeById(Long id) {
        storage.remove(id);
    }
}
