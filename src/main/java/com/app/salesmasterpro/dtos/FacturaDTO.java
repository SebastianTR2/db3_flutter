package com.app.salesmasterpro.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {
    private Long id;

    @NotBlank(message = "El numero es obligatorio")
    @Size(max = 50, message = "El numero no puede exceder 50 caracteres")
    private String numero;

    @NotNull(message = "El pedido es obligatorio")
    private Long pedidoId;

    private String pedidoCodigo;
    private LocalDateTime fecha;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", message = "El subtotal debe ser mayor o igual a 0")
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", message = "El impuesto debe ser mayor o igual a 0")
    private BigDecimal impuesto;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", message = "El total debe ser mayor o igual a 0")
    private BigDecimal total;

    @Size(max = 50, message = "El estado no puede exceder 50 caracteres")
    private String estado;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
