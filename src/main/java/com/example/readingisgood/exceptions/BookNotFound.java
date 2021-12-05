package com.example.readingisgood.exceptions;

public class BookNotFound extends ReadingIsGoodBaseException {
    public BookNotFound() {
        super("Book not found");
    }
}
