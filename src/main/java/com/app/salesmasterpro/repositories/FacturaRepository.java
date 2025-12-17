package com.app.salesmasterpro.repositories;

import com.app.salesmasterpro.entities.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByNumero(String numero);
    Optional<Factura> findByPedidoId(Long pedidoId);
}
