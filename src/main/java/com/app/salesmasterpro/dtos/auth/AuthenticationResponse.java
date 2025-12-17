package com.app.salesmasterpro.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private boolean success;
    private String message;
    private String token;
    private Long userId;
    private String username;
    private String email;
    private Set<String> roles;
}
