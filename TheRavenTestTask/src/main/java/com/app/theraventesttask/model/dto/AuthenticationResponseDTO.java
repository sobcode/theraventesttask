package com.app.theraventesttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an authentication response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDTO {
    private String token;
    private String email;
}
