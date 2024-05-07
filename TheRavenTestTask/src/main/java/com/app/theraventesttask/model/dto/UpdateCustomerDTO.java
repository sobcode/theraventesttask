package com.app.theraventesttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an update for customer data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerDTO {
    private long id;
    private String fullName;
    private String phone;
}
