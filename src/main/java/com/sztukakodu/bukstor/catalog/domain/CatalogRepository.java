package com.sztukakodu.bukstor.catalog.domain;

import java.util.List;

public interface CatalogRepository {
    List<Book> findAll();
}
