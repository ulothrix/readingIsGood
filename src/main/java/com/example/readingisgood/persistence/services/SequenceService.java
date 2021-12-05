package com.example.readingisgood.persistence.services;

import com.example.readingisgood.persistence.entitites.SequenceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SequenceService {

    private final MongoOperations mongoOperations;

    @Transactional
    public Long getNextSequence(String sequenceName)
    {
        Query query = new Query(Criteria.where("collection").is(sequenceName));
        UpdateDefinition update = new Update().inc("sequenceNumber",1);

        SequenceEntity counter = mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                SequenceEntity.class
        );
        assert counter != null;
        return counter.getSequenceNumber();
    }
}
