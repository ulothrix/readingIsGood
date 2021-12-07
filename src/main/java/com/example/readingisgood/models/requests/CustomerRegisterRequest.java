package com.example.readingisgood.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterRequest implements Serializable {

    @Email
    @NotBlank(message = "Email cannot be left blank")
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotBlank(message = "Password cannot be left blank")
    @NotNull(message = "Password cannot be null")
    private String password;

    private List<String> roles;
}
