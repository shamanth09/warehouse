package com.capgemini.warehouse.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String message;
    private final String details;

    public ErrorResponse(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
