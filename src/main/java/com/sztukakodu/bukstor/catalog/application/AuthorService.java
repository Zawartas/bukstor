package com.sztukakodu.bukstor.catalog.application;

import com.sztukakodu.bukstor.catalog.application.port.AuthorUseCase;
import com.sztukakodu.bukstor.catalog.db.AuthorJpaRepository;
import com.sztukakodu.bukstor.catalog.domain.Author;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService implements AuthorUseCase {

    public final AuthorJpaRepository repository;

    @Override
    public List<Author> findAll() {
        return repository.findAll();
    }
}
