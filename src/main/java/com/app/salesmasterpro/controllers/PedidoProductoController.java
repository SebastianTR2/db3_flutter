package com.app.salesmasterpro.controllers;

import com.app.salesmasterpro.common.ApiResponse;
import com.app.salesmasterpro.dtos.PedidoProductoDTO;
import com.app.salesmasterpro.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/pedido-productos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PedidoProductoController {

    private final PedidoService pedidoService;

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoProductoDTO>> update(
            @PathVariable Long id,
            @RequestBody PedidoProductoDTO dto) {

        PedidoProductoDTO updated = pedidoService.updatePedidoProducto(id, dto);

        ApiResponse<PedidoProductoDTO> response = ApiResponse.<PedidoProductoDTO>builder()
                .success(true)
                .message("Producto del pedido actualizado correctamente")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        pedidoService.deletePedidoProducto(id);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Producto eliminado del pedido correctamente")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
