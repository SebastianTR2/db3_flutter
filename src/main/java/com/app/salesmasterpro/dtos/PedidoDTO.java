package com.app.salesmasterpro.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;

    @NotBlank(message = "El codigo es obligatorio")
    @Size(max = 50, message = "El codigo no puede exceder 50 caracteres")
    private String codigo;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    private String clienteNombre;
    private LocalDateTime fecha;
    private BigDecimal total;

    @Size(max = 50, message = "El estado no puede exceder 50 caracteres")
    private String estado;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    private List<PedidoProductoDTO> productos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
