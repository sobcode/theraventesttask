package com.app.theraventesttask.service;

import com.app.theraventesttask.model.dto.AuthenticationRequestDTO;
import com.app.theraventesttask.model.dto.AuthenticationResponseDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO authenticateCustomer(AuthenticationRequestDTO authenticateRequestDTO);
}
