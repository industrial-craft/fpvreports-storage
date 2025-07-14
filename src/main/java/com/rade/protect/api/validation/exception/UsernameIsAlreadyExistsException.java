package com.rade.protect.api.validation.exception;

public class UsernameIsAlreadyExistsException extends RuntimeException {

    public UsernameIsAlreadyExistsException(String message) {
        super(message);
    }
}
