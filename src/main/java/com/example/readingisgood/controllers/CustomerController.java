package com.example.readingisgood.controllers;

import com.example.readingisgood.models.requests.CustomerLoginRequest;
import com.example.readingisgood.models.requests.CustomerRegisterRequest;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.models.responses.TokenResponse;
import com.example.readingisgood.persistence.entitites.OrderEntity;
import com.example.readingisgood.services.CustomerService;
import com.example.readingisgood.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;

    @PostMapping("register")
    public ReadingIsGoodResponse<Void> customerRegister(@Valid @RequestBody CustomerRegisterRequest customerRegisterRequest) {
        return customerService.registerUser(customerRegisterRequest);
    }

    @PostMapping("login")
    public ReadingIsGoodResponse<TokenResponse> login(@Valid @RequestBody CustomerLoginRequest customerLoginRequest) {
        return customerService.loginUser(customerLoginRequest);
    }

    @GetMapping("orders")
    public ReadingIsGoodResponse<List<OrderEntity>> getOrders(@AuthenticationPrincipal final UserDetails userDetails) {
        return customerService.getOrders(userDetails);
    }
}
