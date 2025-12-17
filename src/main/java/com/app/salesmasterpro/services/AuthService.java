package com.app.salesmasterpro.services;

import com.app.salesmasterpro.dtos.auth.AuthenticationRequest;
import com.app.salesmasterpro.dtos.auth.AuthenticationResponse;
import com.app.salesmasterpro.dtos.auth.RegisterRequest;

public interface AuthService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
}
