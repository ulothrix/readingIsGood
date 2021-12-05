package com.example.readingisgood.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBookDto {
    private long id;
    private String name;
    private String author;
    @JsonProperty("ISBN")
    private String isbn;
}