package com.app.salesmasterpro.services.impl;

import com.app.salesmasterpro.dtos.ClienteDTO;
import com.app.salesmasterpro.entities.Cliente;
import com.app.salesmasterpro.exceptions.ResourceNotFoundException;
import com.app.salesmasterpro.repositories.ClienteRepository;
import com.app.salesmasterpro.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found with id: " + id));
        return toDTO(cliente);
    }

    @Override
    public ClienteDTO create(ClienteDTO clienteDTO) {
        Cliente cliente = toEntity(clienteDTO);
        Cliente saved = clienteRepository.save(cliente);
        return toDTO(saved);
    }

    @Override
    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        Cliente existing = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found with id: " + id));

        existing.setNombre(clienteDTO.getNombre());
        existing.setApellido(clienteDTO.getApellido());
        existing.setNit(clienteDTO.getNit());
        existing.setEmail(clienteDTO.getEmail());
        existing.setTelefono(clienteDTO.getTelefono());
        existing.setDireccion(clienteDTO.getDireccion());
        existing.setCiudad(clienteDTO.getCiudad());
        existing.setPais(clienteDTO.getPais());

        Cliente updated = clienteRepository.save(existing);
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente not found with id: " + id);
        }
        clienteRepository.deleteById(id);
    }

    private ClienteDTO toDTO(Cliente cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .nit(cliente.getNit())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .ciudad(cliente.getCiudad())
                .pais(cliente.getPais())
                .createdAt(cliente.getCreatedAt())
                .updatedAt(cliente.getUpdatedAt())
                .build();
    }

    private Cliente toEntity(ClienteDTO dto) {
        return Cliente.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .nit(dto.getNit())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .pais(dto.getPais())
                .build();
    }
}
