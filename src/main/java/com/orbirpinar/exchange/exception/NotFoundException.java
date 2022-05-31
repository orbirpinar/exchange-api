package com.orbirpinar.exchange.exception;

public class NotFoundException extends RuntimeException{


    public NotFoundException(String filterValue) {
        super("Resource not found with " + filterValue);
    }
}
