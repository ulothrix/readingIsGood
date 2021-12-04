package com.example.readingisgood.persistence.repositories;

import com.example.readingisgood.persistence.entitites.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
}
