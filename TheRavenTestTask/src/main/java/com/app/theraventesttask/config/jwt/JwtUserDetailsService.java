package com.app.theraventesttask.config.jwt;

import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class implementing Spring Security's UserDetailsService for JWT authentication.
 *
 * This class is responsible for loading user details during authentication,
 * specifically used for retrieving customer information based on their email.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final CustomerService customerService;

    /**
     * Constructor for JwtUserDetailsService.
     *
     * @param customerService Service for managing customer data.
     */
    @Autowired
    public JwtUserDetailsService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Loads user details by email during authentication.
     *
     * @param email Email of the user(customer) to retrieve.
     * @return UserDetails object representing the user details.
     * @throws UsernameNotFoundException Thrown if the user with the provided email is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerService.findCustomerByEmail(email
        );

        if(customer == null){
            throw new UsernameNotFoundException("User with email " + email + " not found!");
        }

        return JwtUserFactory.createJwtUser(customer);
    }
}
