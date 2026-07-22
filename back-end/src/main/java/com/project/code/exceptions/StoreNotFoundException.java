package com.project.code.exceptions;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(Long id) {
        super("Store with id " + id + " not found");
    }
}