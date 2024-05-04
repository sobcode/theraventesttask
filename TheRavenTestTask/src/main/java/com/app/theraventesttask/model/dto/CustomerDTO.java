package com.app.theraventesttask.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String password;

    public static CustomerDTO fromUpdateCustomerDTO(UpdateCustomerDTO updateCustomerDTO) {
        return new CustomerDTO(updateCustomerDTO.getFullName(),
                null, updateCustomerDTO.getPhone(), null);
    }
}
