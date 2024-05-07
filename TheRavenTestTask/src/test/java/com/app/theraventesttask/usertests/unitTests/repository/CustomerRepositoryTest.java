package com.app.theraventesttask.usertests.unitTests.repository;

import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomer() {
        Customer customer = customerRepository.save(new Customer("Frank Sinatra", "frank@gmail.com",
                "+380998887766", true, "FrSi01", new Date().getTime()));

        assertTrue(customer.getId() != 0);
    }

    @Test
    public void testFindAllCustomers() {
        for(int i = 0; i < 3; i++) {
            customerRepository.save(new Customer("Frank Sinatra", "frank" + i + "@gmail.com",
                    "+380998887766", true, "FrSi01", new Date().getTime()));
        }

        assertEquals(3, customerRepository.findAll().size());
    }

    @Test
    public void testFindCustomerByEmail() {
        String email = "frank@gmail.com";
        String fullName = "Frank Sinatra";
        Customer customer = new Customer(fullName, email, "+380998887766",
                true, "FrSi01", new Date().getTime());

        customerRepository.save(customer);

        assertEquals(fullName, customerRepository.findCustomerByEmail(email).getFullName());
    }

    @Test
    public void testFindCustomerById() {
        String fullName = "Frank Sinatra";
        Customer customer = new Customer(fullName, "frank@gmail.com", "+380998887766",
                true, "FrSi01", new Date().getTime());

        long id = customerRepository.save(customer).getId();

        assertEquals(fullName, customerRepository.findCustomerById(id).getFullName());
    }

    @Test
    public void testFindAllByFullNameContainsAndEmailContainsAndPhoneContains() {
        int numberOfSimilarCustomers = 3;
        for(int i = 0; i < numberOfSimilarCustomers; i++) {
            customerRepository.save(new Customer("Frank Sinatra", "frank" + i + "@gmail.com",
                    "+380998887766", true, "FrSi01", new Date().getTime()));
        }
        customerRepository.save(new Customer("Barbara Ku", "barbara@gmail.com",
                "+380998887766", true, "FrSi01", new Date().getTime()));

        assertEquals(numberOfSimilarCustomers, customerRepository
                .findAllByFullNameContainsAndEmailContainsAndPhoneContainsAndIsActiveIsTrue("",
                        "frank", "", Pageable.unpaged()).getTotalElements());
    }
}
