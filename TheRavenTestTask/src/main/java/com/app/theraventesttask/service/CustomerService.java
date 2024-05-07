package com.app.theraventesttask.service;

import com.app.theraventesttask.exception.InvalidInputFormatException;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.CustomerDTO;
import com.app.theraventesttask.model.dto.CustomerResponseDTO;
import com.app.theraventesttask.model.dto.PaginatedCustomersResponseDTO;
import com.app.theraventesttask.model.dto.UpdateCustomerDTO;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Customer getCustomerByEmail(String email);

    CustomerResponseDTO createCustomer(CustomerDTO customerDTO) throws InvalidInputFormatException;

    PaginatedCustomersResponseDTO getCustomers(String fullName, String email, String phone, Pageable pageable);

    CustomerResponseDTO getCustomerById(long id);

    CustomerResponseDTO updateCustomer(UpdateCustomerDTO customerDTO, long id, boolean partialUpdateAllowed)
            throws InvalidInputFormatException;

    void deleteCustomer(long id);
}
