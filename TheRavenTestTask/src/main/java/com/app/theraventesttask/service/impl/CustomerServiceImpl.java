package com.app.theraventesttask.service.impl;

import com.app.theraventesttask.exception.AuthenticationException;
import com.app.theraventesttask.exception.InvalidInputFormatException;
import com.app.theraventesttask.helper.UpdateHelper;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.CustomerDTO;
import com.app.theraventesttask.model.dto.CustomerResponseDTO;
import com.app.theraventesttask.model.dto.PaginatedCustomersResponseDTO;
import com.app.theraventesttask.model.dto.UpdateCustomerDTO;
import com.app.theraventesttask.repository.CustomerRepository;
import com.app.theraventesttask.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementation of the CustomerService for managing customer-related operations.
 * <p>
 * This class provides functionality for adding, retrieving, updating, and deleting customers,
 * as well as checking the validity of customer data inputs.
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UpdateHelper updateHelper;

    /**
     * Constructor for CustomerServiceImpl.
     *
     * @param customerRepository Repository for managing customer data.
     * @param passwordEncoder    PasswordEncoder for encoding customer passwords.
     */
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
                               UpdateHelper updateHelper) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.updateHelper = updateHelper;
    }

    /**
     * Finds a customer by email.
     *
     * @param email Email of the customer to find.
     * @return Customer entity representing the found customer.
     */
    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    /**
     * Adds a new customer based on the provided CustomerDTO.
     *
     * @param customerDTO DTO containing customer information.
     * @return CustomerResponseDTO representing the added customer.
     * @throws InvalidInputFormatException Thrown if the input data does not meet the required format.
     */
    @Override
    public CustomerResponseDTO createCustomer(CustomerDTO customerDTO) throws InvalidInputFormatException {
        checkCustomerDTO(customerDTO);

        if (customerRepository.findCustomerByEmail(customerDTO.getEmail()) != null) { // checks if there is a customer with such email
            throw new AuthenticationException("Customer with email " + customerDTO.getEmail() +
                    " is already exists!");
        }

        customerDTO.setPassword(passwordEncoder.encode(customerDTO.getPassword())); // encodes password before customer creation
        Customer createdCustomer = customerRepository.save(Customer.getCustomerFromCustomerDTO(customerDTO));

        return CustomerResponseDTO.fromCustomer(createdCustomer);
    }

    /**
     * Retrieves a paginated list of customers based on the specified search criteria.
     *
     * @param fullName The full name of the customer to search for (partial match).
     * @param email The email address of the customer to search for (partial match).
     * @param phone The phone number of the customer to search for (partial match).
     * @param pageable Pagination information for the result set.
     * @return A PaginatedCustomersResponseDTO containing the list of customers matching the specified criteria
     *         along with pagination details.
     */
    @Override
    public PaginatedCustomersResponseDTO getCustomers(String fullName, String email, String phone, Pageable pageable) {
        Page<Customer> customersPage = customerRepository
                .findAllByFullNameContainsAndEmailContainsAndPhoneContainsAndIsActiveIsTrue(
                        fullName, email, phone, pageable);

        List<CustomerResponseDTO> customers = customersPage.stream()
                .map(CustomerResponseDTO::fromCustomer)
                .toList();

        return new PaginatedCustomersResponseDTO(customers, customersPage.getTotalElements(),
                customersPage.getTotalPages());
    }

    /**
     * Retrieves a customer by ID.
     *
     * @param id ID of the customer to retrieve.
     * @return CustomerResponseDTO representing the retrieved customer.
     * @throws NullPointerException Thrown if the customer with the provided ID is not found or is inactive.
     */
    @Override
    public CustomerResponseDTO getCustomerById(long id) {
        Customer customer = customerRepository.findCustomerById(id);

        if (!customer.getIsActive()) {
            throw new NullPointerException("Customer with such id was deleted.");
        }

        return CustomerResponseDTO.fromCustomer(customer);
    }

    /**
     * Updates the customer with the specified ID based on the provided UpdateCustomerDTO.
     *
     * @param customerDTO The UpdateCustomerDTO containing the updated customer information.
     * @param id The ID of the customer to be updated.
     * @param partialUpdateAllowed A boolean flag indicating whether partial update is allowed.
     * @return A CustomerResponseDTO representing the updated customer.
     * @throws InvalidInputFormatException if the provided customerDTO is invalid.
     * @throws NullPointerException if the customer with the specified ID is not found or has been deleted.
     * @throws IllegalArgumentException if partialUpdateAllowed is false and not all required fields are specified.
     * @throws RuntimeException if an unexpected error occurs during the update process.
     */
    @Override
    public CustomerResponseDTO updateCustomer(UpdateCustomerDTO customerDTO, long id, boolean partialUpdateAllowed)
            throws InvalidInputFormatException {
        checkCustomerDTO(CustomerDTO.fromUpdateCustomerDTO(customerDTO));
        Customer customer = customerRepository.findCustomerById(id);

        if (!customer.getIsActive()) {
            throw new NullPointerException("Customer with such an id has been deleted.");
        }

        try {
            if (!partialUpdateAllowed && !updateHelper.checkIfFieldsAreNonNull(customerDTO)) {
                throw new IllegalArgumentException("You need to specify all the required fields to update with 'put'.");
            }
            updateHelper.customerPatcher(customer, customerDTO);
            customer.setUpdated(new Date().getTime());
            customer = customerRepository.save(customer);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return CustomerResponseDTO.fromCustomer(customer);
    }

    /**
     * Deletes a customer based on the provided ID.
     *
     * @param id ID of the customer to delete.
     * @throws NullPointerException Thrown if the customer with the provided ID is not found.
     */
    @Override
    public void deleteCustomer(long id) {
        Customer customer = customerRepository.findCustomerById(id);
        customer.setActive(false);

        customerRepository.save(customer);
    }

    /**
     * Checks the validity of input data in the CustomerDTO.
     *
     * @param customerDTO CustomerDTO containing input data.
     * @throws InvalidInputFormatException Thrown if the input data does not meet the required format.
     */
    private void checkCustomerDTO(CustomerDTO customerDTO) throws InvalidInputFormatException {
        String fullNameRegex = "^.{2,50}$",
                emailRegex = "^(?=.{2,100}$)[^@]*@[^@]*$",
                phoneRegex = "^\\+\\d{5,13}$";

        if (customerDTO.getFullName() != null && !Pattern.matches(fullNameRegex, customerDTO.getFullName()) ||
                (customerDTO.getEmail() != null && !Pattern.matches(emailRegex, customerDTO.getEmail())) ||
                (customerDTO.getPhone() != null && !Pattern.matches(phoneRegex, customerDTO.getPhone()))) {
            throw new InvalidInputFormatException("Invalid format of input data.");
        }
    }
}
