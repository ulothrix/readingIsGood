package com.example.readingisgood.exceptions;

public class BookNotFoundException extends ReadingIsGoodBaseException {
    public BookNotFoundException() {
        super("Book not found");
    }
}
