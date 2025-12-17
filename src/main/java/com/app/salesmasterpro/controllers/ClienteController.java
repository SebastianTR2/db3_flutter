package com.app.salesmasterpro.controllers;

import com.app.salesmasterpro.common.ApiResponse;
import com.app.salesmasterpro.dtos.ClienteDTO;
import com.app.salesmasterpro.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteDTO>>> findAll() {
        List<ClienteDTO> clientes = clienteService.findAll();
        ApiResponse<List<ClienteDTO>> response = ApiResponse.<List<ClienteDTO>>builder()
                .success(true)
                .message("Clientes retrieved successfully")
                .data(clientes)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> findById(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.findById(id);
        ApiResponse<ClienteDTO> response = ApiResponse.<ClienteDTO>builder()
                .success(true)
                .message("Cliente found")
                .data(cliente)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteDTO>> create(
            @jakarta.validation.Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO created = clienteService.create(clienteDTO);
        ApiResponse<ClienteDTO> response = ApiResponse.<ClienteDTO>builder()
                .success(true)
                .message("Cliente created successfully")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> update(@PathVariable Long id,
            @jakarta.validation.Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO updated = clienteService.update(id, clienteDTO);
        ApiResponse<ClienteDTO> response = ApiResponse.<ClienteDTO>builder()
                .success(true)
                .message("Cliente updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        clienteService.delete(id);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Cliente deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
