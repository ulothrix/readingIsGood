package com.example.readingisgood.exceptions;

public class StatisticsNotFoundException extends ReadingIsGoodBaseException {
    public StatisticsNotFoundException() {
        super("Statistics not found for given user");
    }
}
