package com.app.theraventesttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an exception response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDTO {
    private int status;
    private String message;
    private String exceptionName;
}
