package com.sztukakodu.bukstor.catalog.application.port;

import com.sztukakodu.bukstor.catalog.domain.Author;

import java.util.List;

public interface AuthorUseCase {
    List<Author> findAll();
}
