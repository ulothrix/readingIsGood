package com.example.readingisgood.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private List<OrderBookDto> bookList;
    @DecimalMin("0.01")
    private Double totalCost = 0.0;
    private Integer purchasedBookCount = 0;
    private LocalDateTime orderPlacedDate;
}
