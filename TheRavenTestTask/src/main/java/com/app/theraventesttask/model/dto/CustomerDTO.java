package com.app.theraventesttask.model.dto;

import com.app.theraventesttask.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a customer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String fullName;
    private String email;
    private String phone;
    private String password;

    /**
     * Creates a CustomerDTO from an UpdateCustomerDTO.
     *
     * @param updateCustomerDTO The UpdateCustomerDTO object to create the CustomerDTO from.
     * @return A CustomerDTO object.
     */
    public static CustomerDTO fromUpdateCustomerDTO(UpdateCustomerDTO updateCustomerDTO) {
        return new CustomerDTO(updateCustomerDTO.getFullName(),
                null, updateCustomerDTO.getPhone(), null);
    }

    /**
     * Creates a CustomerDTO from a Customer entity.
     *
     * @param customer The Customer entity to create the CustomerDTO from.
     * @return A CustomerDTO object.
     */
    public static CustomerDTO fromCustomer(Customer customer) {
        return new CustomerDTO(customer.getFullName(), customer.getEmail(),
                customer.getPhone(), customer.getPassword());
    }
}
