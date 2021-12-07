package com.example.readingisgood.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersTimeIntervalRequest {
    private String startDate;
    private String endDate;
}
