package com.project.code.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Product with id " + productId + " is not found");
    }
}