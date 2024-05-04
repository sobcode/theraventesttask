package com.app.theraventesttask.service;

import com.app.theraventesttask.exception.InvalidInputFormatException;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.CustomerDTO;
import com.app.theraventesttask.model.dto.CustomerResponseDTO;
import com.app.theraventesttask.model.dto.UpdateCustomerDTO;

import java.util.List;

public interface CustomerService {
    Customer findCustomerByEmail(String email);

    Customer addCustomer(CustomerDTO customerDTO) throws InvalidInputFormatException;

    List<CustomerResponseDTO> getAllCustomers();

    CustomerResponseDTO getCustomerById(long id);

    CustomerResponseDTO updateCustomer(UpdateCustomerDTO customerDTO, long id, boolean partialUpdateAllowed)
            throws InvalidInputFormatException;

    void deleteCustomer(long id);
}
