package com.example.readingisgood.persistence.repositories;

import com.example.readingisgood.persistence.entitites.StatisticEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticRepository extends MongoRepository<StatisticEntity,Long> {
}
