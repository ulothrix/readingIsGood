package com.example.readingisgood.exceptions;

public class OrderNotFoundException extends ReadingIsGoodBaseException {
    public OrderNotFoundException() {
        super("Order not found!");
    }
}
