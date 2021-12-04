package com.example.readingisgood.persistence.entitites;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "orders")
public class OrderEntity {

    @Transient
    private static final String ORDER_SEQUENCE = "order_sequence";

    @Id
    private long id;
    @DBRef
    private List<BookEntity> books;

    @CreatedDate
    private Date createdDate;
}
