package com.example.readingisgood.persistence.entitites;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "db_sequence")
public class SequenceEntity {
    @Id
    private String collection;
    private Long sequenceNumber;
}
