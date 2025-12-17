package com.app.salesmasterpro.controllers;

import com.app.salesmasterpro.common.ApiResponse;
import com.app.salesmasterpro.dtos.FacturaDTO;
import com.app.salesmasterpro.services.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FacturaDTO>>> findAll() {
        List<FacturaDTO> facturas = facturaService.findAll();
        ApiResponse<List<FacturaDTO>> response = ApiResponse.<List<FacturaDTO>>builder()
                .success(true)
                .message("Facturas retrieved successfully")
                .data(facturas)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacturaDTO>> findById(@PathVariable Long id) {
        FacturaDTO factura = facturaService.findById(id);
        ApiResponse<FacturaDTO> response = ApiResponse.<FacturaDTO>builder()
                .success(true)
                .message("Factura found")
                .data(factura)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FacturaDTO>> create(
            @jakarta.validation.Valid @RequestBody FacturaDTO facturaDTO) {
        FacturaDTO created = facturaService.create(facturaDTO);
        ApiResponse<FacturaDTO> response = ApiResponse.<FacturaDTO>builder()
                .success(true)
                .message("Factura created successfully")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FacturaDTO>> update(@PathVariable Long id,
            @jakarta.validation.Valid @RequestBody FacturaDTO facturaDTO) {
        FacturaDTO updated = facturaService.update(id, facturaDTO);
        ApiResponse<FacturaDTO> response = ApiResponse.<FacturaDTO>builder()
                .success(true)
                .message("Factura updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        facturaService.delete(id);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Factura deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
