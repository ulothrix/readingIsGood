package com.example.readingisgood.persistence.entitites;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "customers")
@EnableMongoAuditing
public class CustomerEntity {

    @Transient
    public static final String CUSTOMER_SEQUENCE = "customer_sequence";

    @Id
    @Getter
    @Setter
    private Long id;

    @Indexed(unique = true)
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;

    @DBRef
    @Getter
    @Setter
    private List<OrderEntity> orders;

    @Getter
    @Setter
    private Set<SimpleGrantedAuthority> roles = new HashSet<>();
}
