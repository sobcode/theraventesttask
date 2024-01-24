package com.app.theraventesttask.controller.exceptionHandler;

import com.app.theraventesttask.model.dto.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for controllers.
 *
 * This class provides a centralized way to handle exceptions thrown by controllers.
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    /**
     * Handles exceptions thrown by controllers and generates an appropriate response.
     *
     * @param exception Exception thrown by the controller.
     * @return ResponseEntity containing the response DTO for the exception.
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleException(Exception exception) {
        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    /**
     * Generates a ResponseEntity with an ExceptionResponseDTO.
     *
     * @param status     HTTP status code for the response.
     * @param message    Error message to be included in the response.
     * @param simpleName Simple name of the exception class.
     * @return ResponseEntity containing the response DTO with error details.
     */
    private ResponseEntity<ExceptionResponseDTO> provideResponseEntity(HttpStatus status,
                                                                       String message, String simpleName) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(status.value(), message, simpleName);

        return new ResponseEntity<>(responseDTO, status);
    }
}

