package com.app.salesmasterpro.services;

import com.app.salesmasterpro.dtos.FacturaDTO;

import java.util.List;

public interface FacturaService {
    List<FacturaDTO> findAll();
    FacturaDTO findById(Long id);
    FacturaDTO create(FacturaDTO facturaDTO);
    FacturaDTO update(Long id, FacturaDTO facturaDTO);
    void delete(Long id);
}
