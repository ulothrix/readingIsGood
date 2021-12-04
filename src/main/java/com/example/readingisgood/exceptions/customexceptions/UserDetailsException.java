package com.example.readingisgood.exceptions.customexceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsException extends RuntimeException {
    private String message;
    public UserDetailsException(String message){
        this.message = message;
    }
}
