package com.app.salesmasterpro.services.impl;

import com.app.salesmasterpro.dtos.UsuarioDTO;
import com.app.salesmasterpro.entities.Role;
import com.app.salesmasterpro.entities.Usuario;
import com.app.salesmasterpro.repositories.UsuarioRepository;
import com.app.salesmasterpro.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO getById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioDTO update(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Update allowed fields
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        // Password is explicitly NOT updated here

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return mapToDTO(updatedUsuario);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO mapToDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .roles(usuario.getRoles().stream()
                        .map(Role::getNombre)
                        .collect(Collectors.toList()))
                .build();
    }
}
