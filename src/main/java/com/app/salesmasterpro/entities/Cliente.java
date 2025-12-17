package com.app.salesmasterpro.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Cliente Entity - Mapped to 'clientes' table
 */
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    @Builder.Default
    private String apellido = "";

    @Column(nullable = false, unique = true, length = 50)
    private String nit;

    @Column(nullable = false, length = 255)
    @Builder.Default
    private String email = "";

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String telefono = "";

    @Column(columnDefinition = "TEXT", nullable = false)
    @Builder.Default
    private String direccion = "";

    @Column(nullable = false, length = 100)
    @Builder.Default
    private String ciudad = "";

    @Column(nullable = false, length = 100)
    @Builder.Default
    private String pais = "";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
