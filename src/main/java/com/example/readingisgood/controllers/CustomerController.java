package com.example.readingisgood.controllers;


import com.example.readingisgood.models.requests.CustomerLoginRequest;
import com.example.readingisgood.models.requests.CustomerRegisterRequest;
import com.example.readingisgood.models.responses.OrderResponse;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.models.responses.TokenResponse;
import com.example.readingisgood.security.CustomerDetails;
import com.example.readingisgood.services.AuthenticationService;
import com.example.readingisgood.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AuthenticationService authenticationService;

    @PostMapping("register")
    public ReadingIsGoodResponse<Void> customerRegister(@Valid @RequestBody CustomerRegisterRequest customerRegisterRequest) {
        return authenticationService.registerUser(customerRegisterRequest);
    }

    @PostMapping("login")
    public ReadingIsGoodResponse<TokenResponse> login(@Valid @RequestBody CustomerLoginRequest customerLoginRequest) {
        return authenticationService.loginUser(customerLoginRequest);
    }

    @GetMapping("orders")
    public ReadingIsGoodResponse<OrderResponse> getOrders(@AuthenticationPrincipal final CustomerDetails customerDetails)
    {
        return customerService.getOrders(customerDetails);
    }
}
