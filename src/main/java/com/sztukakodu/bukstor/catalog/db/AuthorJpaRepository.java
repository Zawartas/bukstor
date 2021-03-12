package com.sztukakodu.bukstor.catalog.db;

import com.sztukakodu.bukstor.catalog.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

}
