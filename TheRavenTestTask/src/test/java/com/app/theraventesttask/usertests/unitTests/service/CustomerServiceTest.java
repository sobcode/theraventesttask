package com.app.theraventesttask.usertests.unitTests.service;

import com.app.theraventesttask.exception.AuthenticationException;
import com.app.theraventesttask.exception.InvalidInputFormatException;
import com.app.theraventesttask.helper.UpdateHelper;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.CustomerDTO;
import com.app.theraventesttask.model.dto.CustomerResponseDTO;
import com.app.theraventesttask.model.dto.PaginatedCustomersResponseDTO;
import com.app.theraventesttask.model.dto.UpdateCustomerDTO;
import com.app.theraventesttask.repository.CustomerRepository;
import com.app.theraventesttask.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {
    @InjectMocks
    private CustomerServiceImpl customerService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UpdateHelper updateHelper;
    @Mock
    PasswordEncoder passwordEncoder;

    private final List<Customer> customers;

    {
        customers = new ArrayList<Customer>();
        customers.add(new Customer("Frank Sinatra", "frank@gmail.com",
                "+380998887766", true, "FrSi01", new Date().getTime()));
        customers.add(new Customer("Pavlo Biruk", "pavlo@gmail.com",
                "+380978887766", true, "PaBi02", new Date().getTime()));
    }

    @Test
    public void testCreateCustomerWithProperData_thenReturnsProperResponseDTO() throws InvalidInputFormatException {
        Customer initCustomer = customers.get(0);

        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(null);
        when(customerRepository.save(any(Customer.class))).thenReturn(initCustomer);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        CustomerResponseDTO responseDTO = customerService.createCustomer(CustomerDTO.fromCustomer(initCustomer));

        assertEquals(initCustomer.getEmail(), responseDTO.getEmail());
    }

    @Test
    public void testCreateCustomerWithInvalidData_thenThrowsInvalidInputFormatException() {
        Customer initCustomer = customers.get(0);
        initCustomer.setPhone("123");

        assertThrows(InvalidInputFormatException.class, () -> customerService.createCustomer(
                CustomerDTO.fromCustomer(initCustomer)));
    }

    @Test
    public void testCreateCustomerWithRegisteredEmail_thenThrowsAuthenticationException() {
        Customer intiCustomer = customers.get(0);

        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(new Customer());

        assertThrows(AuthenticationException.class, () -> customerService.createCustomer(
                CustomerDTO.fromCustomer(intiCustomer)));
    }

    @Test
    public void testGetCustomerByEmail_thenReturnsCustomerWithSameEmail() {
        Customer initCustomer = customers.get(0);

        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(initCustomer);

        Customer respCustomer = customerService.getCustomerByEmail(initCustomer.getEmail());

        assertEquals(initCustomer.getEmail(), respCustomer.getEmail());
    }

    @Test
    public void testGetCustomers_thenReturnsAllCustomers() {
        Page<Customer> customersPage = new PageImpl<>(customers);

        when(customerRepository.findAllByFullNameContainsAndEmailContainsAndPhoneContainsAndIsActiveIsTrue(
                anyString(), anyString(), anyString(), any(Pageable.class))).thenReturn(customersPage);

        PaginatedCustomersResponseDTO resp =
                customerService.getCustomers("", "", "", Pageable.unpaged());

        assertEquals(customersPage.getTotalElements(), resp.getCustomerList().size());
    }

    @Test
    public void testGetCustomerById_thenReturnsRequiredCustomer() {
        long id = 1L;
        Customer initCustomer = customers.get(0);

        when(customerRepository.findCustomerById(anyLong())).thenReturn(initCustomer);

        CustomerResponseDTO resp = customerService.getCustomerById(id);

        assertEquals(initCustomer.getEmail(), resp.getEmail());
    }

    @Test
    public void testGetDeletedCustomerById_thenThrowsNullPointerException() {
        long id = 1L;
        Customer initCustomer = customers.get(0);
        initCustomer.setActive(false);

        when(customerRepository.findCustomerById(anyLong())).thenReturn(initCustomer);

        assertThrows(NullPointerException.class, () -> customerService.getCustomerById(id));
    }

    @Test
    public void testUpdateCustomerWithProperDataAndWithoutPartialUpdate_thenReturnsProperResponseDTO()
            throws NoSuchFieldException, IllegalAccessException, InvalidInputFormatException {
        long id = 1L;
        UpdateCustomerDTO customerDTO = new UpdateCustomerDTO(1,
                "Frank Sinatra", "+380998887766");
        Customer customer = customers.get(0);

        when(customerRepository.findCustomerById(anyLong())).thenReturn(customer);
        doNothing().when(updateHelper).customerPatcher(any(Customer.class), any(UpdateCustomerDTO.class));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(updateHelper.checkIfFieldsAreNonNull(any(UpdateCustomerDTO.class))).thenReturn(true);

        CustomerResponseDTO resp = customerService.updateCustomer(customerDTO, id, false);

        assertEquals(customer.getEmail(), resp.getEmail());
        assertTrue(customer.getUpdated() != 0);
    }

    @Test
    public void testUpdateCustomerWithNullValuesAndWithoutPartialUpdate_thenThrowsIllegalArgumentException()
            throws IllegalAccessException {
        long id = 1L;
        UpdateCustomerDTO customerDTO = new UpdateCustomerDTO(1,
                "Frank Sinatra", null);
        Customer customer = customers.get(0);

        when(customerRepository.findCustomerById(anyLong())).thenReturn(customer);
        when(updateHelper.checkIfFieldsAreNonNull(any(UpdateCustomerDTO.class))).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(customerDTO,
                id, false));
    }

    @Test
    public void testUpdateCustomerWithProperDataAndWithPartialUpdate_thenReturnsProperResponseDTO()
            throws NoSuchFieldException, IllegalAccessException, InvalidInputFormatException {
        long id = 1L;
        UpdateCustomerDTO customerDTO = new UpdateCustomerDTO(1,
                "Frank Sinatra", "+380998887766");
        Customer customer = customers.get(0);

        when(customerRepository.findCustomerById(anyLong())).thenReturn(customer);

        doNothing().when(updateHelper).customerPatcher(any(Customer.class), any(UpdateCustomerDTO.class));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDTO resp = customerService.updateCustomer(customerDTO, id, true);

        assertEquals(customer.getEmail(), resp.getEmail());
        assertTrue(customer.getUpdated() != 0);
    }

    @Test
    public void testUpdateDeletedCustomer_thenThrowsNullPointerException() {
        long id = 1L;
        UpdateCustomerDTO customerDTO = new UpdateCustomerDTO(1,
                "Frank Sinatra", "+380998887766");
        Customer customer = customers.get(0);
        customer.setActive(false);

        when(customerRepository.findCustomerById(anyLong())).thenReturn(customer);

        assertThrows(NullPointerException.class, () -> customerService.updateCustomer(customerDTO,
                id, false));
    }

    @Test
    public void testUpdateCustomerWithWrongData_thenThrowsInvalidInputFormatException() {
        long id = 1L;
        UpdateCustomerDTO customerDTO = new UpdateCustomerDTO(1,
                "Frank Sinatra", "380998887766");

        assertThrows(InvalidInputFormatException.class, () -> customerService.updateCustomer(customerDTO,
                id, false));
    }

    @Test
    public void testDeleteCustomer_thenTheCustomerMarkedAsDeleted() {
        long id = 1L;
        Customer initCustomer = customers.get(0);

        when(customerRepository.findCustomerById(anyLong())).thenReturn(initCustomer);
        when(customerRepository.save(any(Customer.class))).thenReturn(initCustomer);

        customerService.deleteCustomer(id);

        assertFalse(initCustomer.getIsActive());
    }
}
