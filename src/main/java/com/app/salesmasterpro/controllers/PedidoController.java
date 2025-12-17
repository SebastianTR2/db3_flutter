package com.app.salesmasterpro.controllers;

import com.app.salesmasterpro.common.ApiResponse;
import com.app.salesmasterpro.dtos.PedidoDTO;
import com.app.salesmasterpro.dtos.PedidoProductoDTO;
import com.app.salesmasterpro.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<PedidoDTO>>> findAll() {
        System.out.println("DEBUG: PedidoController.findAll() called");
        List<PedidoDTO> pedidos = pedidoService.findAll();
        ApiResponse<List<PedidoDTO>> response = ApiResponse.<List<PedidoDTO>>builder()
                .success(true)
                .message("Pedidos retrieved successfully")
                .data(pedidos)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoDTO>> findById(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.findById(id);
        ApiResponse<PedidoDTO> response = ApiResponse.<PedidoDTO>builder()
                .success(true)
                .message("Pedido found")
                .data(pedido)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<PedidoDTO>> create(@jakarta.validation.Valid @RequestBody PedidoDTO pedidoDTO) {
        System.out.println("DEBUG: PedidoController.create() called");
        PedidoDTO created = pedidoService.create(pedidoDTO);
        ApiResponse<PedidoDTO> response = ApiResponse.<PedidoDTO>builder()
                .success(true)
                .message("Pedido created successfully")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoDTO>> update(@PathVariable Long id,
            @jakarta.validation.Valid @RequestBody PedidoDTO pedidoDTO) {
        PedidoDTO updated = pedidoService.update(id, pedidoDTO);
        ApiResponse<PedidoDTO> response = ApiResponse.<PedidoDTO>builder()
                .success(true)
                .message("Pedido updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        pedidoService.delete(id);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Pedido deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/productos")
    public ResponseEntity<ApiResponse<PedidoDTO>> addProductoToPedido(
            @PathVariable Long id,
            @RequestBody PedidoProductoDTO productoDTO) {
        PedidoDTO updated = pedidoService.addProductoToPedido(id, productoDTO);
        ApiResponse<PedidoDTO> response = ApiResponse.<PedidoDTO>builder()
                .success(true)
                .message("Producto agregado al pedido exitosamente")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
