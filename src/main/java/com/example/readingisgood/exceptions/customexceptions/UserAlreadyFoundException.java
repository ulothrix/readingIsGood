package com.example.readingisgood.exceptions.customexceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAlreadyFoundException extends RuntimeException {
    private String message;
    public UserAlreadyFoundException(String message){
        this.message = message;
    }
}
