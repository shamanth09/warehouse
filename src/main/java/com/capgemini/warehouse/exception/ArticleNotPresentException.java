package com.capgemini.warehouse.exception;

public class ArticleNotPresentException extends RuntimeException {
    public ArticleNotPresentException(String message){
        super(message);
    }

}
