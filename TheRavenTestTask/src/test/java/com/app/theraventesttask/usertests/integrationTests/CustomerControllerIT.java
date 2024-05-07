package com.app.theraventesttask.usertests.integrationTests;

import com.app.theraventesttask.config.jwt.JwtTokenProvider;
import com.app.theraventesttask.controller.CustomerController;
import com.app.theraventesttask.exception.InvalidInputFormatException;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.*;
import com.app.theraventesttask.repository.CustomerRepository;
import com.app.theraventesttask.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

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

    private final List<Customer> customers;

    {
        customers = new ArrayList<Customer>();
        customers.add(new Customer("Frank Sinatra", "frank@gmail.com",
                "+380998887766", true, "FrSi01", new Date().getTime()));
        customers.add(new Customer("Pavlo Biruk", "pavlo@gmail.com",
                "+380978887766", true, "PaBi02", new Date().getTime()));
    }

    @AfterEach
    public void cleanData(){
        customerRepository.deleteAll();
    }

    @Test
    public void testAddCustomer_thenItIsAddedToDatabase() throws InvalidInputFormatException {
        Customer initCustomer = customers.get(0);

        customerController.createCustomer(CustomerDTO.fromCustomer(initCustomer));
        Customer customer = customerRepository.findCustomerByEmail(initCustomer.getEmail());

        assertEquals(initCustomer.getFullName(), customer.getFullName());
    }

    @Test
    public void testAuthenticate_thenReturnsProperJWT() throws InvalidInputFormatException {
        Customer initCustomer = customers.get(0);

        customerService.createCustomer(CustomerDTO.fromCustomer(initCustomer));
        ResponseEntity<AuthenticationResponseDTO> responseEntity =
                customerController.authenticate(new AuthenticationRequestDTO(initCustomer.getEmail(),
                        initCustomer.getPassword()));
        String token = Objects.requireNonNull(responseEntity.getBody()).getToken();

        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    public void testReadCustomers_thenReturnsResponseWithCorrectNumberOfCustomers() {
        customerRepository.saveAll(customers);

        PaginatedCustomersResponseDTO resp = customerController.readCustomers("", "", "",
                Pageable.unpaged()).getBody();

        assert resp != null;
        assertEquals(customers.size(), resp.getNumberOfItems());
    }

    @Test
    public void testReadCustomersWithFiltering_thenReturnsResponseWithSearchedCustomer() {
        Customer initCustomer = new Customer("John Dortmund", "john@gmail.com",
                "+380979797799", true, "JoDo03", new Date().getTime());

        customerRepository.save(initCustomer);
        customerRepository.saveAll(customers);

        PaginatedCustomersResponseDTO resp = customerController.readCustomers("Jo", "hn@", "+3",
                Pageable.unpaged()).getBody();

        assert resp != null;
        assertEquals(1, resp.getNumberOfItems());
        assertEquals(initCustomer.getEmail(), resp.getCustomerList().get(0).getEmail());
    }

    @Test
    public void testReadCustomerById_thenReturnsResponseWithCustomerWithSuchId() {
        Customer initCustomer = customers.get(0);

        initCustomer = customerRepository.save(initCustomer);

        CustomerResponseDTO resp = customerController.readCustomerById(initCustomer.getId()).getBody();

        assert resp != null;
        assertEquals(initCustomer.getId(), resp.getId());
        assertEquals(initCustomer.getEmail(), resp.getEmail());
    }

    @Test
    public void testUpdateCustomerWithAllFieldsSpecified_thenCustomerIsUpdated() throws InvalidInputFormatException {
        String newName = "New Name";
        String newPhone = "+380999999999";
        Customer initCustomer = customers.get(0);

        initCustomer = customerRepository.save(initCustomer);

        customerController.updateCustomer(initCustomer.getId(), new UpdateCustomerDTO(1, newName, newPhone));

        Customer retrievedCustomer = customerRepository.findCustomerByEmail(initCustomer.getEmail());

        assertEquals(newName, retrievedCustomer.getFullName());
        assertEquals(newPhone, retrievedCustomer.getPhone());
    }

    @Test
    public void testUpdateCustomerWithNullValue_thenThrowsIllegalArgumentException() throws InvalidInputFormatException {
        Customer initCustomer = customers.get(0);

        initCustomer = customerRepository.save(initCustomer);

        Customer finalInitCustomer = initCustomer;
        assertThrows(IllegalArgumentException.class, () -> customerController.updateCustomer(finalInitCustomer.getId(), new UpdateCustomerDTO(1,
                "Jorah Mormont", null)));
    }

    @Test
    public void testPartialUpdateCustomerWithNullValue_thenCustomerIsUpdated() throws InvalidInputFormatException {
        String newName = "Jorah Mormont";
        Customer initCustomer = customers.get(0);

        initCustomer = customerRepository.save(initCustomer);

        customerController.partialUpdateCustomer(initCustomer.getId(), new UpdateCustomerDTO(1, newName, null));

        Customer retrievedCustomer = customerRepository.findCustomerByEmail(initCustomer.getEmail());

        assertEquals(newName, retrievedCustomer.getFullName());
    }

    @Test
    public void testDeleteCustomer_thenCustomerIsDeleted() throws InvalidInputFormatException {
        Customer initCustomer = customers.get(0);

        initCustomer = customerRepository.save(initCustomer);

        customerController.deleteCustomer(initCustomer.getId());

        Customer retrievedCustomer = customerRepository.findCustomerByEmail(initCustomer.getEmail());

        assertFalse(retrievedCustomer.getIsActive());
    }
}
