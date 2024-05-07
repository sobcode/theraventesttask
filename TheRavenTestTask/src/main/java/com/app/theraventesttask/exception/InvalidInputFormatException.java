package com.app.theraventesttask.exception;

/**
 * Exception thrown to indicate an invalid input format.
 */
public class InvalidInputFormatException extends Exception{
    public InvalidInputFormatException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidInputFormatException(String msg) {
        super(msg);
    }
}
