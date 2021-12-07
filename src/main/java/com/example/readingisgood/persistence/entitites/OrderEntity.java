package com.example.readingisgood.persistence.entitites;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.DecimalMin;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@ToString
public class OrderEntity {

    @Transient
    public static final String ORDER_SEQUENCE = "order_sequence";

    @Id
    @Getter
    @Setter
    private Long id;

    @DBRef
    @Getter
    @Setter
    private List<BookEntity> books;

    @Getter
    @Setter
    @DecimalMin("0.01")
    private Double totalCost = 0.0;

    @Getter
    @Setter
    private Integer purchasedBookCount = 0;

    @Getter
    @Setter
    private LocalDateTime orderPlacedDate;
}
