package com.police.evisitor.exception;

public class UserAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExists(String message) {
        super(message);
    }

}