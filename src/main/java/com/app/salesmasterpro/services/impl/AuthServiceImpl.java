package com.app.salesmasterpro.services.impl;

import com.app.salesmasterpro.dtos.auth.AuthenticationRequest;
import com.app.salesmasterpro.dtos.auth.AuthenticationResponse;
import com.app.salesmasterpro.dtos.auth.RegisterRequest;
import com.app.salesmasterpro.entities.Role;
import com.app.salesmasterpro.entities.Usuario;
import com.app.salesmasterpro.repositories.RoleRepository;
import com.app.salesmasterpro.repositories.UsuarioRepository;
import com.app.salesmasterpro.security.JwtService;
import com.app.salesmasterpro.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if username or email already exists
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return AuthenticationResponse.builder()
                    .success(false)
                    .message("Username already exists")
                    .build();
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return AuthenticationResponse.builder()
                    .success(false)
                    .message("Email already exists")
                    .build();
        }

        // Get roles or assign default
        Set<Role> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (String roleName : request.getRoles()) {
                roleRepository.findByNombre(roleName).ifPresent(roles::add);
            }
        }

        // If no roles specified, assign VENDEDOR role
        if (roles.isEmpty()) {
            roleRepository.findByNombre("VENDEDOR").ifPresent(roles::add);
        }

        // Create user
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .roles(roles)
                .build();

        usuarioRepository.save(usuario);

        // Generate JWT
        String token = jwtService.generateToken(usuario);

        return AuthenticationResponse.builder()
                .success(true)
                .message("User registered successfully")
                .token(token)
                .userId(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .roles(usuario.getRoles().stream().map(Role::getNombre).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        // Find user by username or email
        Usuario usuario = usuarioRepository.findByUsername(request.getIdentifier())
                .orElseGet(() -> usuarioRepository.findByEmail(request.getIdentifier())
                        .orElse(null));

        if (usuario == null) {
            return AuthenticationResponse.builder()
                    .success(false)
                    .message("Invalid credentials")
                    .build();
        }

        // Authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuario.getUsername(),
                        request.getPassword()));

        // Generate JWT
        String token = jwtService.generateToken(usuario);

        return AuthenticationResponse.builder()
                .success(true)
                .message("Login successful")
                .token(token)
                .userId(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .roles(usuario.getRoles().stream().map(Role::getNombre).collect(Collectors.toSet()))
                .build();
    }
}
