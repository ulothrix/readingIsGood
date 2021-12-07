package com.example.readingisgood.models.dtos;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private List<OrderBookDto> bookList;
}
