package com.app.theraventesttask.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String fullName;
    private String email;
    private String phone;
    private String password;

    public static CustomerDTO fromUpdateCustomerDTO(UpdateCustomerDTO updateCustomerDTO) {
        return new CustomerDTO(updateCustomerDTO.getFullName(),
                null, updateCustomerDTO.getPhone(), null);
    }
}
