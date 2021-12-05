package com.example.readingisgood.exceptions;

public class CustomerNotFoundException extends ReadingIsGoodBaseException {

    public CustomerNotFoundException() {
        super("Customer not found!");
    }

}
