package com.sztukakodu.bukstor.catalog.db;

import com.sztukakodu.bukstor.catalog.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

}
