package com.example.readingisgood.exceptions;

public class OrderNotFoundInTimeIntervalException extends ReadingIsGoodBaseException {
    public OrderNotFoundInTimeIntervalException() {
        super("Order not found in given time interval");
    }
}
