package com.app.theraventesttask.controller.exceptionHandler;

import com.app.theraventesttask.exception.AuthenticationException;
import com.app.theraventesttask.model.dto.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for controllers.
 * This class provides a centralized way to handle exceptions thrown by controllers.
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleNullPointerExceptions(NullPointerException exception) {
        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleAuthenticationExceptions(AuthenticationException exception) {
        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleIllegalArgumentExceptionExceptions(
            IllegalArgumentException exception) {
        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleRuntimeExceptions(RuntimeException exception) {
        return provideResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleException(Exception exception) {
        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    private ResponseEntity<ExceptionResponseDTO> provideResponseEntity(HttpStatus status,
                                                                       String message, String simpleName) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(status.value(), message, simpleName);

        return new ResponseEntity<>(responseDTO, status);
    }
}

