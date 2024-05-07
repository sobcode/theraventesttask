package com.app.theraventesttask.usertests.unitTests.service;

import com.app.theraventesttask.config.jwt.JwtTokenProvider;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.AuthenticationRequestDTO;
import com.app.theraventesttask.model.dto.AuthenticationResponseDTO;
import com.app.theraventesttask.repository.CustomerRepository;
import com.app.theraventesttask.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void testAuthenticateCustomerWithExistingUser_thenReturnsProperResponse() {
        String token = "token";
        AuthenticationRequestDTO requestDTO = new AuthenticationRequestDTO("frank@gmail.com", "FrSi01");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "frank", "FrSi01");
        Customer customer = new Customer("Frank Sinatra", "frank@gmail.com",
                "+380998887766", true, "FrSi01", new Date().getTime());


        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(customer);
        when(jwtTokenProvider.createToken(anyString(), anyString())).thenReturn(token);

        AuthenticationResponseDTO resp = authenticationService.authenticateCustomer(requestDTO);

        assertEquals(token, resp.getToken());
        assertEquals(customer.getEmail(), resp.getEmail());
    }

    @Test
    public void testAuthenticateCustomerWithNotExistingUser_thenThrowsNoSuchElementException() {
        AuthenticationRequestDTO requestDTO = new AuthenticationRequestDTO("frank@gmail.com", "FrSi01");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "frank", "FrSi01");


        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> authenticationService.authenticateCustomer(requestDTO));
    }
}
