package com.example.readingisgood.persistence.entitites;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "books")
public class BookEntity {

    @Transient
    private static final String BOOK_SEQUENCE = "book_sequence";

    @Id
    private long id;
    private String name;
    private int stock;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;
}
