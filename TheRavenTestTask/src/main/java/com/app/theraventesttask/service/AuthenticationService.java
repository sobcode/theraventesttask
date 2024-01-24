package com.app.theraventesttask.service;

import com.app.theraventesttask.model.dto.AuthenticateRequestDTO;
import com.app.theraventesttask.model.dto.AuthenticationResponseDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO authenticateCustomer(AuthenticateRequestDTO authenticateRequestDTO);
}
