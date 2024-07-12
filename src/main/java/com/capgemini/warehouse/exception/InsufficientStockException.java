package com.capgemini.warehouse.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String s) {
        super(s);
    }
}
