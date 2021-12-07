package com.example.readingisgood.persistence.repositories;

import com.example.readingisgood.persistence.entitites.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookRepository extends MongoRepository<BookEntity, Long> {
    boolean existsBookEntityByIsbn(String isbn);

    Optional<BookEntity> findBookEntityByIsbn(String isbn);

}
