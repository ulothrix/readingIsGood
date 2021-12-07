package com.example.readingisgood.services;

import com.example.readingisgood.models.responses.MessageResponse;
import com.example.readingisgood.models.responses.OrderResponse;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.persistence.entitites.CustomerEntity;
import com.example.readingisgood.persistence.repositories.CustomerRepository;
import com.example.readingisgood.security.CustomerDetails;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderService orderService;

    public ReadingIsGoodResponse<OrderResponse> getOrders(CustomerDetails customerDetails) {

        Optional<CustomerEntity> customer = customerRepository.findByEmail(customerDetails.getEmail());

        ReadingIsGoodResponse<OrderResponse> response = new ReadingIsGoodResponse<>();
        OrderResponse orderResponse = OrderResponse
                .builder()
                .orders(new ArrayList<>())
                .build();

        if (customer.isPresent() && !Collections.isEmpty(customer.get().getOrders())) {
            response = orderService.constructOrdersToDto(customer.get().getOrders(), orderResponse);
        } else {
            MessageResponse messageResponse = new MessageResponse("No order found!");
            response.setMessage(messageResponse);
        }

        return response;
    }
}
