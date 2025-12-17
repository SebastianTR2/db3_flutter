package com.app.salesmasterpro.services;

import com.app.salesmasterpro.dtos.UsuarioDTO;
import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> getAll();

    UsuarioDTO getById(Long id);

    UsuarioDTO update(Long id, UsuarioDTO dto);

    void delete(Long id);
}
