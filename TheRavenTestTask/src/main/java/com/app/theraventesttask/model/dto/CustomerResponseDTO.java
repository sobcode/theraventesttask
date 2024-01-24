package com.app.theraventesttask.model.dto;

import com.app.theraventesttask.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerResponseDTO {
    private long id;
    private String fullName;
    private String email;
    private String phone;

    public static CustomerResponseDTO fromCustomer(Customer customer) {
        return new CustomerResponseDTO(customer.getId(), customer.getFullName(),
                customer.getEmail(), customer.getPhone());
    }
}
