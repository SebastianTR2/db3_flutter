package com.app.salesmasterpro.security;

import com.app.salesmasterpro.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Try to find by username first, then by email
        return usuarioRepository.findByUsername(identifier)
            .orElseGet(() -> usuarioRepository.findByEmail(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + identifier)));
    }
}
