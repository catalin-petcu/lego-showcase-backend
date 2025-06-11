package com.legohub.exception;

public class LegoAlreadyExistsException extends RuntimeException {
    public LegoAlreadyExistsException(String message) {
        super(message);
    }
}
