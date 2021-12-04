package com.example.readingisgood.persistence.repositories;

import com.example.readingisgood.persistence.entitites.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<BookEntity, Long> {
}
