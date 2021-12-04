package com.example.readingisgood.models.requests;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CustomerLoginRequest implements Serializable {
    @Email
    @NotBlank(message = "Email cannot be left blank")
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotBlank(message = "Password cannot be left blank")
    @NotNull(message = "Password cannot be null")
    private String password;
}
