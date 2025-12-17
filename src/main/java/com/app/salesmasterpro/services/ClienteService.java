package com.app.salesmasterpro.services;

import com.app.salesmasterpro.dtos.ClienteDTO;

import java.util.List;

public interface ClienteService {
    List<ClienteDTO> findAll();
    ClienteDTO findById(Long id);
    ClienteDTO create(ClienteDTO clienteDTO);
    ClienteDTO update(Long id, ClienteDTO clienteDTO);
    void delete(Long id);
}
