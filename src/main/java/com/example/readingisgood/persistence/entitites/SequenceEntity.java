package com.example.readingisgood.persistence.entitites;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "db_sequence")
@ToString
public class SequenceEntity {

    @Id
    @Getter
    @Setter
    private String collection;

    @Getter
    @Setter
    private Long sequenceNumber;
}
