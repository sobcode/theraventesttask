package com.app.theraventesttask.service.impl;

import com.app.theraventesttask.config.jwt.JwtTokenProvider;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.AuthenticationRequestDTO;
import com.app.theraventesttask.model.dto.AuthenticationResponseDTO;
import com.app.theraventesttask.repository.CustomerRepository;
import com.app.theraventesttask.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Implementation of the AuthenticationService for authenticating customers.
 *
 * This class provides functionality for authenticating customers using Spring Security
 * and generating JWT tokens for successful authentication.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final CustomerRepository customerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor for AuthenticationServiceImpl.
     *
     * @param customerRepository    Repository for managing customer data.
     * @param jwtTokenProvider      JwtTokenProvider for handling JWT-related operations.
     * @param authenticationManager AuthenticationManager for managing authentication.
     */
    @Autowired
    public AuthenticationServiceImpl(CustomerRepository customerRepository,
                           JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.customerRepository = customerRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates a customer based on the provided credentials and generates a JWT token.
     *
     * @param authenticateRequestDTO DTO containing customer authentication credentials.
     * @return AuthenticationResponseDTO containing the JWT token and customer email.
     * @throws NoSuchElementException Thrown if the customer with the provided email is not found.
     */
    @Override
    public AuthenticationResponseDTO authenticateCustomer(AuthenticationRequestDTO authenticateRequestDTO) {
        // Authenticate the customer using Spring Security's AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticateRequestDTO.getEmail(),
                        authenticateRequestDTO.getPassword()));

        Customer customer = customerRepository.findCustomerByEmail(authenticateRequestDTO.getEmail());

        if(customer == null){
            throw new NoSuchElementException("User with username " + authenticateRequestDTO.getEmail() +
                    " not found!");
        }

        String token = jwtTokenProvider.createToken(customer.getEmail(), "Admin");

        return new AuthenticationResponseDTO(token, customer.getEmail());
    }
}
