package com.example.readingisgood.security;

import com.example.readingisgood.persistence.entitites.CustomerEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class CustomerDetails implements UserDetails {

    private long id;
    private String email;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private CustomerDetails(long userId, String userEmail, String userPassword, Collection<? extends GrantedAuthority> userAuthorities) {
        this.id = userId;
        this.email = userEmail;
        this.username = userEmail;
        this.password = userPassword;
        this.authorities = userAuthorities;
    }

    // todo: builder kullanabilir miyiz ?
    public static CustomerDetails getUserDetails(CustomerEntity customerEntity) {
        return new CustomerDetails(customerEntity.getId(), customerEntity.getEmail(), customerEntity.getPassword(), customerEntity.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
