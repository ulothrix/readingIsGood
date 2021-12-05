package com.example.readingisgood.exceptions;

public class OrderNotFound extends ReadingIsGoodBaseException {
    public OrderNotFound() {
        super("Order not found!");
    }
}
