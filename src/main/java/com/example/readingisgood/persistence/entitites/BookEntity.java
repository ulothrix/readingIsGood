package com.example.readingisgood.persistence.entitites;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Document(collection = "books")
@ToString
public class BookEntity {

    @Transient
    public static final String BOOK_SEQUENCE = "book_sequence";

    @Id
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Field(name = "ISBN")
    @Indexed(unique = true)
    @Getter
    @Setter
    private String isbn;

    @Getter
    @Setter
    private String author;

    @Min(0)
    @Getter
    @Setter
    private int stock;

    @DecimalMin("0.01")
    @Getter
    @Setter
    private Double price;

    @Version
    @Getter
    @Setter
    private int version;
}
