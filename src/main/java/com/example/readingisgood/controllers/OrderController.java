package com.example.readingisgood.controllers;

import com.example.readingisgood.models.dtos.OrderDto;
import com.example.readingisgood.models.requests.OrderRequest;
import com.example.readingisgood.models.requests.OrdersTimeIntervalRequest;
import com.example.readingisgood.models.responses.OrderResponse;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.security.CustomerDetails;
import com.example.readingisgood.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("new")
    public ReadingIsGoodResponse<OrderResponse> placeNewOrder(@RequestBody @Valid OrderRequest orderRequest, @AuthenticationPrincipal final CustomerDetails customerDetails) {
        return orderService.placeNewOrder(orderRequest, customerDetails);
    }

    @GetMapping("detail/{id}")
    public ReadingIsGoodResponse<OrderDto> getOrderDetail(@AuthenticationPrincipal final CustomerDetails customerDetails, @Min(0) @PathVariable("id") Long id) {
        return orderService.getOrderDetail(id, customerDetails);
    }

    @GetMapping("list")
    public ReadingIsGoodResponse<OrderResponse> getOrderByTimeInterval(@AuthenticationPrincipal final CustomerDetails customerDetails, @RequestBody OrdersTimeIntervalRequest ordersTimeIntervalRequest) {
        return orderService.getOrdersByTimeInterval(customerDetails, ordersTimeIntervalRequest);
    }
}
