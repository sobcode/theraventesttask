package com.app.theraventesttask.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown to indicate an authentication error related to JWT token processing.
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
