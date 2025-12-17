package com.app.salesmasterpro.services;

import com.app.salesmasterpro.dtos.ProductoDTO;

import java.util.List;

public interface ProductoService {
    List<ProductoDTO> findAll();
    ProductoDTO findById(Long id);
    ProductoDTO create(ProductoDTO productoDTO);
    ProductoDTO update(Long id, ProductoDTO productoDTO);
    void delete(Long id);
}
