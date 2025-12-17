package com.app.salesmasterpro.controllers;

import com.app.salesmasterpro.common.ApiResponse;
import com.app.salesmasterpro.dtos.ProductoDTO;
import com.app.salesmasterpro.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> findAll() {
        List<ProductoDTO> productos = productoService.findAll();
        ApiResponse<List<ProductoDTO>> response = ApiResponse.<List<ProductoDTO>>builder()
                .success(true)
                .message("Productos retrieved successfully")
                .data(productos)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> findById(@PathVariable Long id) {
        ProductoDTO producto = productoService.findById(id);
        ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                .success(true)
                .message("Producto found")
                .data(producto)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductoDTO>> create(
            @jakarta.validation.Valid @RequestBody ProductoDTO productoDTO) {
        ProductoDTO created = productoService.create(productoDTO);
        ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                .success(true)
                .message("Producto created successfully")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> update(@PathVariable Long id,
            @jakarta.validation.Valid @RequestBody ProductoDTO productoDTO) {
        ProductoDTO updated = productoService.update(id, productoDTO);
        ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                .success(true)
                .message("Producto updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        productoService.delete(id);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Producto deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
