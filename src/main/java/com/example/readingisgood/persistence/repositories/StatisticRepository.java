package com.example.readingisgood.persistence.repositories;

import com.example.readingisgood.persistence.entitites.StatisticEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StatisticRepository extends MongoRepository<StatisticEntity, Long> {
    List<StatisticEntity> getStatisticEntityByEmail(String email);
}
