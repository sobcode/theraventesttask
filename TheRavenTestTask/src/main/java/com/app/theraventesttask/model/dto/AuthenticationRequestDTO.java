package com.app.theraventesttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an authentication request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDTO {
    private String email;
    private String password;
}
