package com.example.readingisgood.exceptions;

public class StockChangeException extends ReadingIsGoodBaseException {
    public StockChangeException(String isbn) {
        super("Stock information of " + isbn + " has been changed");
    }
}
