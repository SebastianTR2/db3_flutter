package com.app.salesmasterpro.bootstrap;

import com.app.salesmasterpro.entities.Role;
import com.app.salesmasterpro.entities.Usuario;
import com.app.salesmasterpro.repositories.RoleRepository;
import com.app.salesmasterpro.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        if (roleRepository.count() == 0) {
            Role adminRole = Role.builder()
                .nombre("ADMIN")
                .descripcion("Administrator role")
                .build();
            
            Role userRole = Role.builder()
                .nombre("USER")
                .descripcion("User role")
                .build();

            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            System.out.println("✅ Roles created: ADMIN, USER");
        }

        // Create admin user if doesn't exist
        if (!usuarioRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByNombre("ADMIN").orElseThrow();
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            Usuario admin = Usuario.builder()
                .username("admin")
                .email("admin@salesmasterpro.com")
                .password(passwordEncoder.encode("admin123"))
                .nombre("Admin")
                .apellido("SalesMasterPro")
                .telefono("000000000")
                .roles(roles)
                .build();

            usuarioRepository.save(admin);

            System.out.println("✅ Admin user created: admin / admin123");
        }
    }
}
