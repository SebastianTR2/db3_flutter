package com.app.salesmasterpro.repositories;

import com.app.salesmasterpro.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNit(String nit);

    Optional<Cliente> findByEmail(String email);
}
