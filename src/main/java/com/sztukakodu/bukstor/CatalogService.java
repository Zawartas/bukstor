package com.sztukakodu.bukstor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private Map<Long, Book> storage = new ConcurrentHashMap<>();

    public CatalogService() {
        storage.put(1L, new Book(1L, "Pan Tadeusz", "Mickiewicz", 2049));
        storage.put(2L, new Book(2L, "1984", "Orwell", 1949));
        storage.put(3L, new Book(3L, "Nowy Wspaniały Swiat", "Huxley", 1971));
        storage.put(4L, new Book(4L, "Pan Wołodyjowski", "Sienkiewicz", 1891));

    }

    List<Book> findByTitle(String title) {
        return storage.values()
                .stream()
                .filter(book -> book.title.startsWith(title))
                .collect(Collectors.toList());
    }
}
