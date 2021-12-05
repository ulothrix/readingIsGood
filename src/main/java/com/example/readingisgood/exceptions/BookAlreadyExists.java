package com.example.readingisgood.exceptions;

public class BookAlreadyExists extends ReadingIsGoodBaseException {
    public BookAlreadyExists() {
        super("Book with given ISBN number is already exists!");
    }
}
