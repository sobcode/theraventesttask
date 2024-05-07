package com.app.theraventesttask.usertests.unitTests.controller;

import com.app.theraventesttask.controller.CustomerController;
import com.app.theraventesttask.filter.JwtTokenFilter;
import com.app.theraventesttask.model.dto.*;
import com.app.theraventesttask.service.AuthenticationService;
import com.app.theraventesttask.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateCustomer_thenHttpStatusIsOkAndReturnsCorrectResponse() throws Exception {
        String fullName = "Frank Sinatra";
        String requestBody = objectMapper.writeValueAsString(new CustomerDTO(fullName, "frank@gmail.com",
                "+380998887766", "FrSi01"));
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(1, fullName,
                "frank@gmail.com", "+380998887766");

        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));
    }

    @Test
    public void testReadCustomers_thenHttpStatusIsOkAndReturnsCorrectResponse() throws Exception {
        PaginatedCustomersResponseDTO responseDTO = new PaginatedCustomersResponseDTO();

        when(customerService.getCustomers(anyString(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(get("/api/customers")
                        .param("fullName", "Joe")
                        .param("email", "joe")
                        .param("phone", "+380")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));
    }

    @Test
    public void testReadCustomerById_thenHttpStatusIsOkAndReturnsCorrectResponse() throws Exception {
        long id = 1L;
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(1, "Frank Sinatra",
                "frank@gmail.com", "+38099887766");

        when(customerService.getCustomerById(anyLong())).thenReturn(responseDTO);

        mockMvc.perform(get("/api/customers/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));
    }

    @Test
    public void testUpdateCustomer_thenHttpStatusIsOkAndReturnsCorrectResponse() throws Exception {
        long id = 1L;
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(1, "Frank Sinatra",
                "frank@gmail.com", "+38099887766");
        String requestBody = objectMapper.writeValueAsString(new UpdateCustomerDTO(1, "Frank Sinatra",
                "+380998887766"));

        when(customerService.updateCustomer(any(UpdateCustomerDTO.class), anyLong(), anyBoolean()))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));
    }

    @Test
    public void testPartialUpdateCustomer_thenHttpStatusIsOkAndReturnsCorrectResponse() throws Exception {
        long id = 1L;
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(1, "Frank Sinatra",
                "frank@gmail.com", "+38099887766");
        String requestBody = objectMapper.writeValueAsString(new UpdateCustomerDTO(1, "Frank Sinatra",
                "+380998887766"));

        when(customerService.updateCustomer(any(UpdateCustomerDTO.class), anyLong(), anyBoolean()))
                .thenReturn(responseDTO);

        mockMvc.perform(patch("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));
    }

    @Test
    public void testDeleteCustomer_thenHttpStatusIsOk() throws Exception {
        long id = 1L;

        doNothing().when(customerService).deleteCustomer(anyLong());

        mockMvc.perform(delete("/api/customers/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticate() throws Exception {
        long id = 1L;
        AuthenticationResponseDTO responseDTO = new AuthenticationResponseDTO("1d3", "frank@gmail.com");
        String requestBody = objectMapper.writeValueAsString(new AuthenticationRequestDTO(
                "frank@gmail.com", "FrSi01"));

        when(authenticationService.authenticateCustomer(any(AuthenticationRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/customers/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));

    }
}
