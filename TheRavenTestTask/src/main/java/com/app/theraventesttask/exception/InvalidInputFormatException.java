package com.app.theraventesttask.exception;

public class InvalidInputFormatException extends Exception{
    public InvalidInputFormatException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidInputFormatException(String msg) {
        super(msg);
    }
}
