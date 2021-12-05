package com.example.readingisgood.models.requests;

import lombok.Data;

@Data
public class OrdersTimeIntervalRequest {
    private String startDate;
    private String endDate;
}
