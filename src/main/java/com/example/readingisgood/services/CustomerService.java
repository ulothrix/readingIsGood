package com.example.readingisgood.services;

import com.example.readingisgood.exceptions.customexceptions.UserAlreadyFoundException;
import com.example.readingisgood.models.requests.CustomerLoginRequest;
import com.example.readingisgood.models.requests.CustomerRegisterRequest;
import com.example.readingisgood.models.responses.MessageResponse;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.models.responses.TokenResponse;
import com.example.readingisgood.persistence.entitites.CustomerEntity;
import com.example.readingisgood.persistence.entitites.OrderEntity;
import com.example.readingisgood.persistence.repositories.CustomerRepository;
import com.example.readingisgood.persistence.services.SequenceService;
import com.example.readingisgood.security.UserDetailsServiceImpl;
import com.example.readingisgood.utils.JwtUtil;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.readingisgood.configuration.constants.MessageConstants.REGISTERED;
import static com.example.readingisgood.configuration.constants.SecurityConstants.DEFAULT_ROLE;
import static com.example.readingisgood.persistence.entitites.CustomerEntity.USER_SEQUENCE;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SequenceService sequenceService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public ReadingIsGoodResponse<Void> registerUser(CustomerRegisterRequest customerRegisterRequest) {
        if (customerRepository.existsByEmail(customerRegisterRequest.getEmail())) {
            throw new UserAlreadyFoundException("User already found");
        } else {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(sequenceService.getNextSequence(USER_SEQUENCE));
            customerEntity.setEmail(customerRegisterRequest.getEmail());
            customerEntity.setPassword(passwordEncoder.encode(customerRegisterRequest.getPassword()));
            customerEntity.setRoles(setAuthorities(customerRegisterRequest.getRoles()));
            customerRepository.save(customerEntity);

            MessageResponse messageResponse = new MessageResponse(REGISTERED);

            return new ReadingIsGoodResponse<>(messageResponse);
        }
    }

    public ReadingIsGoodResponse<TokenResponse> loginUser(CustomerLoginRequest customerLoginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                customerLoginRequest.getEmail(),
                customerLoginRequest.getPassword(),
                List.of(new SimpleGrantedAuthority(DEFAULT_ROLE))));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(customerLoginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(jwt);
        tokenResponse.setExpiration(jwtUtil.extractExpiration(jwt).toInstant().getEpochSecond());

        return new ReadingIsGoodResponse<>(tokenResponse);
    }

    public ReadingIsGoodResponse<List<OrderEntity>> getOrders(UserDetails userDetails) {
        Optional<CustomerEntity> customer = customerRepository.findByEmail(userDetails.getUsername());
        ReadingIsGoodResponse<List<OrderEntity>> response = new ReadingIsGoodResponse<>();
        if (customer.isPresent()) {
            response.setData(customer.get().getOrders());
        } else {
            MessageResponse messageResponse = new MessageResponse("No order found!");
            response.setMessage(messageResponse);
        }
        return response;
    }

    private Set<SimpleGrantedAuthority> setAuthorities(List<String> authorityStrings) {
        if (Collections.isEmpty(authorityStrings)) {
            return Set.of(new SimpleGrantedAuthority(DEFAULT_ROLE));
        }
        return authorityStrings
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

    }
}
