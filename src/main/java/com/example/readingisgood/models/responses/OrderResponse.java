package com.example.readingisgood.models.responses;

import com.example.readingisgood.models.dtos.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private List<OrderDto> orders;
}
