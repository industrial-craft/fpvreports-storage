package com.rade.protect.api.validation.exception;

public class RequiredFieldMustNotBeNullException extends RuntimeException {

    public RequiredFieldMustNotBeNullException(String message) {
        super(message);
    }

}
