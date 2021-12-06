package com.example.readingisgood.persistence.services;

import com.example.readingisgood.persistence.entitites.SequenceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class SequenceServiceTest {

    @Mock
    private MongoOperations mongoOperations;
    @InjectMocks
    private SequenceService sequenceService;

    @Test
    void it_should_increment_identity_number_of_a_collection() {
        String SEQUENCE_NAME = "test_sequence";
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
        MockedStatic<FindAndModifyOptions> mockedStatic = mockStatic(FindAndModifyOptions.class);

        Query query = new Query(Criteria.where("collection").is(SEQUENCE_NAME));
        UpdateDefinition update = new Update().inc("sequenceNumber", 1);

        SequenceEntity sequenceEntity = new SequenceEntity();
        sequenceEntity.setSequenceNumber(5L);
        sequenceEntity.setCollection(SEQUENCE_NAME);

        SequenceEntity sequenceEntity2 = new SequenceEntity();
        sequenceEntity2.setSequenceNumber(6L);
        sequenceEntity2.setCollection(SEQUENCE_NAME);

        mockedStatic.when(FindAndModifyOptions::options).thenReturn(findAndModifyOptions);
        given(mongoOperations.findAndModify(
                query,
                update,
                findAndModifyOptions.returnNew(true).upsert(true),
                SequenceEntity.class)).willReturn(sequenceEntity2);

        long nextSequenceNumber = sequenceService.getNextSequence(SEQUENCE_NAME);

        assertThat(nextSequenceNumber).isEqualTo(6L);
    }
}