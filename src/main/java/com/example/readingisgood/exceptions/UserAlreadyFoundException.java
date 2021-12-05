package com.example.readingisgood.exceptions;

public class UserAlreadyFoundException extends ReadingIsGoodBaseException {

    public UserAlreadyFoundException() {
        super("User already found");
    }
}
