package com.app.theraventesttask.controller;

import com.app.theraventesttask.exception.InvalidInputFormatException;
import com.app.theraventesttask.model.dto.*;
import com.app.theraventesttask.service.AuthenticationService;
import com.app.theraventesttask.service.CustomerService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller class handling customer-related operations.
 * <p>
 * This class defines RESTful endpoints for managing customer information.
 * It includes operations such as creating, reading, updating, and deleting customers.
 * Additionally, it provides an endpoint for customer authentication.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final AuthenticationService authenticationService;

    /**
     * Constructor for CustomerController.
     *
     * @param customerService       Service responsible for managing customer data.
     * @param authenticationService Service for customer authentication.
     */
    @Autowired
    public CustomerController(CustomerService customerService, AuthenticationService authenticationService) {
        this.customerService = customerService;
        this.authenticationService = authenticationService;
    }

    /**
     * Endpoint for creating a new customer.
     *
     * @param customerDTO CustomerDTO containing the data for the new customer.
     * @return ResponseEntity containing the response DTO for the created customer.
     * @throws InvalidInputFormatException Thrown when the input data does not meet the required format.
     */
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerDTO customerDTO)
            throws InvalidInputFormatException {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO));
    }

    /**
     * Retrieves a paginated list of customers based on the provided search criteria.
     * Customers can be filtered by specifying parts of the fields fullName, email or phone.
     *
     * @param fullName The full name of the customer to search for.
     * @param email    The email address of the customer to search for.
     * @param phone    The phone number of the customer to search for.
     * @param pageable Pagination information for the result set.
     * @return A ResponseEntity containing a PaginatedCustomersResponseDTO with the list of customers
     *         matching the provided criteria and pagination details.
     */
    @GetMapping
    public ResponseEntity<PaginatedCustomersResponseDTO> readCustomers(@RequestParam(defaultValue = "") String fullName,
                                                                       @RequestParam(defaultValue = "") String email,
                                                                       @RequestParam(defaultValue = "") String phone,
                                                                       Pageable pageable) {
        return ResponseEntity.ok(customerService.getCustomers(fullName, email, phone, pageable));
    }

    /**
     * Endpoint for retrieving a customer by ID.
     *
     * @param id ID of the customer to retrieve.
     * @return ResponseEntity containing the response DTO for the specified customer.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> readCustomerById(@PathVariable @Min(0) long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    /**
     * Endpoint to update a customer's information (all the fields).
     *
     * @param id ID of the customer to update.
     * @param customerDTO UpdateCustomerDTO containing the new data for the customer.
     * @return ResponseEntity containing the response DTO for the updated customer.
     * @throws InvalidInputFormatException Thrown when the input data does not meet the required format.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable @Min(1) long id,
                                                              @RequestBody UpdateCustomerDTO customerDTO)
            throws InvalidInputFormatException {
        return ResponseEntity.ok(customerService.updateCustomer(customerDTO, id, false));
    }

    /**
     * Endpoint to partially update a customer's information (one/some fields).
     *
     * @param id ID of the customer to update.
     * @param customerDTO UpdateCustomerDTO containing the new data for the customer.
     * @return ResponseEntity containing the response DTO for the updated customer.
     * @throws InvalidInputFormatException Thrown when the input data does not meet the required format.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> partialUpdateCustomer(@PathVariable @Min(1) long id,
                                                                     @RequestBody UpdateCustomerDTO customerDTO)
            throws InvalidInputFormatException {
        return ResponseEntity.ok(customerService.updateCustomer(customerDTO, id, true));
    }

    /**
     * Endpoint to delete a customer by ID (set isActive to false).
     *
     * @param id The ID of the customer to delete.
     * @return ResponseEntity indicating success of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable @Min(1) long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for authenticating a customer.
     *
     * @param authenticateRequestDTO AuthenticateRequestDTO containing authentication credentials.
     * @return ResponseEntity containing the response DTO for the authentication result.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody
                                                                  AuthenticationRequestDTO authenticateRequestDTO) {
        return ResponseEntity.ok(authenticationService.authenticateCustomer(authenticateRequestDTO));
    }
}
