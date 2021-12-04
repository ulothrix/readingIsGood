package com.example.readingisgood.security;

import com.example.readingisgood.exceptions.customexceptions.UserDetailsException;
import com.example.readingisgood.persistence.entitites.CustomerEntity;
import com.example.readingisgood.persistence.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomerEntity> customer = customerRepository.findByEmail(username);
        return customer
                .map(UserDetailsImpl::getUserDetails)
                .orElseThrow(() -> new UserDetailsException("UserDetails Error"));
    }
}
