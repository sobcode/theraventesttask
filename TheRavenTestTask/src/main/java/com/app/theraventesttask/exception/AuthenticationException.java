package com.app.theraventesttask.exception;

/**
 * Exception thrown to indicate an authentication error.
 */
public class AuthenticationException extends org.springframework.security.core.AuthenticationException {
    public AuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }
}
