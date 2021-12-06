package com.example.readingisgood.exceptions;

public class BookAlreadyExistException extends ReadingIsGoodBaseException {
    public BookAlreadyExistException() {
        super("Book with given ISBN number is already exists!");
    }
}
