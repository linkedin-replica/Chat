package com.linkedin.replica.chat.exceptions;


public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
