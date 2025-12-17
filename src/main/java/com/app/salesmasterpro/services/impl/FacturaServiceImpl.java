package com.app.salesmasterpro.services.impl;

import com.app.salesmasterpro.dtos.FacturaDTO;
import com.app.salesmasterpro.entities.Factura;
import com.app.salesmasterpro.entities.Pedido;
import com.app.salesmasterpro.exceptions.ResourceNotFoundException;
import com.app.salesmasterpro.repositories.FacturaRepository;
import com.app.salesmasterpro.repositories.PedidoRepository;
import com.app.salesmasterpro.services.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    public List<FacturaDTO> findAll() {
        return facturaRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public FacturaDTO findById(Long id) {
        Factura factura = facturaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Factura not found with id: " + id));
        return toDTO(factura);
    }

    @Override
    public FacturaDTO create(FacturaDTO facturaDTO) {
        Pedido pedido = pedidoRepository.findById(facturaDTO.getPedidoId())
            .orElseThrow(() -> new ResourceNotFoundException("Pedido not found"));

        Factura factura = Factura.builder()
            .numero(facturaDTO.getNumero())
            .pedido(pedido)
            .fecha(facturaDTO.getFecha())
            .subtotal(facturaDTO.getSubtotal())
            .impuesto(facturaDTO.getImpuesto())
            .total(facturaDTO.getTotal())
            .estado(facturaDTO.getEstado())
            .observaciones(facturaDTO.getObservaciones())
            .build();

        Factura saved = facturaRepository.save(factura);
        return toDTO(saved);
    }

    @Override
    public FacturaDTO update(Long id, FacturaDTO facturaDTO) {
        Factura existing = facturaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Factura not found with id: " + id));

        existing.setEstado(facturaDTO.getEstado());
        existing.setObservaciones(facturaDTO.getObservaciones());

        Factura updated = facturaRepository.save(existing);
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Factura not found with id: " + id);
        }
        facturaRepository.deleteById(id);
    }

    private FacturaDTO toDTO(Factura factura) {
        return FacturaDTO.builder()
            .id(factura.getId())
            .numero(factura.getNumero())
            .pedidoId(factura.getPedido().getId())
            .pedidoCodigo(factura.getPedido().getCodigo())
            .fecha(factura.getFecha())
            .subtotal(factura.getSubtotal())
            .impuesto(factura.getImpuesto())
            .total(factura.getTotal())
            .estado(factura.getEstado())
            .observaciones(factura.getObservaciones())
            .createdAt(factura.getCreatedAt())
            .updatedAt(factura.getUpdatedAt())
            .build();
    }
}
