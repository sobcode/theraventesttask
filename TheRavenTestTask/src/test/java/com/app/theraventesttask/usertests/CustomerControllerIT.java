package com.app.theraventesttask.usertests;

import com.app.theraventesttask.config.jwt.JwtTokenProvider;
import com.app.theraventesttask.controller.CustomerController;
import com.app.theraventesttask.exception.InvalidInputFormatException;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.AuthenticateRequestDTO;
import com.app.theraventesttask.model.dto.AuthenticationResponseDTO;
import com.app.theraventesttask.model.dto.CustomerDTO;
import com.app.theraventesttask.repository.CustomerRepository;
import com.app.theraventesttask.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class CustomerControllerIT {
    @Autowired
    private CustomerController customerController;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String fullName = "Pavlo Biruk";
    private String email = "pashka@gmail.com";
    private String phone = "+380989989898";
    private String password = "PavlO1";

    @AfterEach
    public void cleanData(){
        customerRepository.deleteAll();
    }

    @Test
    public void whenAddCustomer_thenItIsAddedToDatabase() throws InvalidInputFormatException {
        customerController.createCustomer(new CustomerDTO(fullName, email, phone, password));
        Customer customer = customerRepository.findCustomerByEmail(email);

        Assertions.assertEquals(fullName, customer.getFullName());
    }

    @Test
    public void whenAuthenticate_thenReceiveProperJWT() throws InvalidInputFormatException {
        customerService.addCustomer(new CustomerDTO(fullName, email, phone, password));
        ResponseEntity<AuthenticationResponseDTO> responseEntity =
                customerController.authenticate(new AuthenticateRequestDTO(email, password));
        String token = responseEntity.getBody().getToken();

        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }
}
