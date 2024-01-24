package com.app.theraventesttask.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateCustomerDTO {
    private long id;
    private String fullName;
    private String phone;
}
