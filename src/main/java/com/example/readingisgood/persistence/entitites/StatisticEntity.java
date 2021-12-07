package com.example.readingisgood.persistence.entitites;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "statistics")
public class StatisticEntity {

    @Transient
    public static final String STATISTIC_SEQUENCE = "statistic_sequence";

    @Id
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String month;

    @Getter
    @Setter
    private Integer totalOrderCount = 0;

    @Getter
    @Setter
    private Integer totalBookCount = 0;

    @Getter
    @Setter
    private Double totalPurchasedAmount = 0.0;
}
