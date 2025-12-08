package com.app.emsx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginRequest
 * -----------------------------------------------------
 * ✔ DTO para recibir datos de inicio de sesión
 * ✔ Usado en /api/auth/login
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * Nombre de usuario o correo electrónico
     */
    private String username;

    /**
     * Contraseña del usuario
     */
    private String password;
}

