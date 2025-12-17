package com.app.salesmasterpro.services.impl;

import com.app.salesmasterpro.dtos.ProductoDTO;
import com.app.salesmasterpro.entities.Producto;
import com.app.salesmasterpro.exceptions.ResourceNotFoundException;
import com.app.salesmasterpro.repositories.ProductoRepository;
import com.app.salesmasterpro.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public List<ProductoDTO> findAll() {
        return productoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoDTO findById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto not found with id: " + id));
        return toDTO(producto);
    }

    @Override
    public ProductoDTO create(ProductoDTO productoDTO) {
        Producto producto = toEntity(productoDTO);
        Producto saved = productoRepository.save(producto);
        return toDTO(saved);
    }

    @Override
    public ProductoDTO update(Long id, ProductoDTO productoDTO) {
        Producto existing = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto not found with id: " + id));

        existing.setNombre(productoDTO.getNombre());
        existing.setDescripcion(productoDTO.getDescripcion());
        existing.setPrecio(productoDTO.getPrecio());
        existing.setStock(productoDTO.getStock());
        existing.setCodigo(productoDTO.getCodigo());
        existing.setCategoria(productoDTO.getCategoria());
        existing.setImagen(productoDTO.getImagen());

        Producto updated = productoRepository.save(existing);
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto not found with id: " + id);
        }
        productoRepository.deleteById(id);
    }

    private ProductoDTO toDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .codigo(producto.getCodigo())
                .categoria(producto.getCategoria())
                .imagen(producto.getImagen())
                .createdAt(producto.getCreatedAt())
                .updatedAt(producto.getUpdatedAt())
                .build();
    }

    private Producto toEntity(ProductoDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .stock(dto.getStock())
                .codigo(dto.getCodigo())
                .categoria(dto.getCategoria())
                .imagen(dto.getImagen())
                .build();
    }
}
