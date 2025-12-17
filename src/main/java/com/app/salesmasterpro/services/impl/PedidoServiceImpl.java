package com.app.salesmasterpro.services.impl;

import com.app.salesmasterpro.dtos.PedidoDTO;
import com.app.salesmasterpro.dtos.PedidoProductoDTO;
import com.app.salesmasterpro.entities.Cliente;
import com.app.salesmasterpro.entities.Pedido;
import com.app.salesmasterpro.entities.PedidoProducto;
import com.app.salesmasterpro.entities.Producto;
import com.app.salesmasterpro.exceptions.ResourceNotFoundException;
import com.app.salesmasterpro.repositories.ClienteRepository;
import com.app.salesmasterpro.repositories.PedidoRepository;
import com.app.salesmasterpro.repositories.ProductoRepository;
import com.app.salesmasterpro.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

        private final PedidoRepository pedidoRepository;
        private final ClienteRepository clienteRepository;
        private final ProductoRepository productoRepository;
        private final com.app.salesmasterpro.repositories.PedidoProductoRepository pedidoProductoRepository;

        @Override
        @Transactional(readOnly = true)
        public List<PedidoDTO> findAll() {
                return pedidoRepository.findAll().stream()
                                .map(this::toDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public PedidoDTO findById(Long id) {
                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Pedido not found with id: " + id));
                return toDTO(pedido);
        }

        @Override
        @Transactional
        public PedidoDTO create(PedidoDTO pedidoDTO) {
                if (pedidoDTO.getClienteId() == null) {
                        System.err.println(
                                        "ERROR: create called with null client ID. This refers to [POST] /api/pedidos. Skipping creation.");
                        return null;
                }
                Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId())
                                .orElseThrow(() -> new ResourceNotFoundException("Cliente not found"));

                Pedido pedido = Pedido.builder()
                                .codigo(pedidoDTO.getCodigo())
                                .cliente(cliente)
                                .fecha(pedidoDTO.getFecha())
                                .total(pedidoDTO.getTotal())
                                .estado(pedidoDTO.getEstado())
                                .observaciones(pedidoDTO.getObservaciones())
                                .build();

                if (pedidoDTO.getProductos() != null) {
                        for (PedidoProductoDTO ppDTO : pedidoDTO.getProductos()) {
                                if (ppDTO.getProductoId() == null) {
                                        throw new IllegalArgumentException("Producto ID must not be null");
                                }
                                Producto producto = productoRepository.findById(ppDTO.getProductoId())
                                                .orElseThrow(() -> new ResourceNotFoundException("Producto not found"));

                                // Verificar stock
                                if (producto.getStock() < ppDTO.getCantidad()) {
                                        throw new IllegalArgumentException(
                                                        "Stock insuficiente para " + producto.getNombre()
                                                                        + ". Disponible: " + producto.getStock());
                                }

                                // Actualizar stock
                                producto.setStock(producto.getStock() - ppDTO.getCantidad());
                                productoRepository.save(producto);

                                PedidoProducto pp = PedidoProducto.builder()
                                                .pedido(pedido)
                                                .producto(producto)
                                                .cantidad(ppDTO.getCantidad())
                                                .precioUnitario(ppDTO.getPrecioUnitario())
                                                .subtotal(ppDTO.getSubtotal())
                                                .build();

                                pedido.getProductos().add(pp);
                        }
                }

                Pedido saved = pedidoRepository.save(pedido);
                return toDTO(saved);
        }

        @Override
        @Transactional
        public PedidoDTO update(Long id, PedidoDTO pedidoDTO) {
                Pedido existing = pedidoRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Pedido not found with id: " + id));

                existing.setEstado(pedidoDTO.getEstado());
                existing.setObservaciones(pedidoDTO.getObservaciones());
                existing.setTotal(pedidoDTO.getTotal());

                Pedido updated = pedidoRepository.save(existing);
                return toDTO(updated);
        }

        @Override
        public void delete(Long id) {
                if (!pedidoRepository.existsById(id)) {
                        throw new ResourceNotFoundException("Pedido not found with id: " + id);
                }
                pedidoRepository.deleteById(id);
        }

        private PedidoDTO toDTO(Pedido pedido) {
                List<PedidoProductoDTO> productos = pedido.getProductos().stream()
                                .map(pp -> {
                                        if (pp.getProducto() == null)
                                                return null;
                                        return PedidoProductoDTO.builder()
                                                        .id(pp.getId())
                                                        .pedidoId(pedido.getId())
                                                        .productoId(pp.getProducto().getId())
                                                        .productoNombre(pp.getProducto().getNombre())
                                                        .cantidad(pp.getCantidad())
                                                        .precioUnitario(pp.getPrecioUnitario())
                                                        .subtotal(pp.getSubtotal())
                                                        .build();
                                })
                                .filter(java.util.Objects::nonNull)
                                .collect(Collectors.toList());

                Long clienteId = null;
                String clienteNombre = "Unknown";
                if (pedido.getCliente() != null) {
                        clienteId = pedido.getCliente().getId();
                        clienteNombre = pedido.getCliente().getNombre()
                                        + (pedido.getCliente().getApellido() != null
                                                        ? " " + pedido.getCliente().getApellido()
                                                        : "");
                }

                return PedidoDTO.builder()
                                .id(pedido.getId())
                                .codigo(pedido.getCodigo())
                                .clienteId(clienteId)
                                .clienteNombre(clienteNombre)
                                .fecha(pedido.getFecha())
                                .total(pedido.getTotal())
                                .estado(pedido.getEstado())
                                .observaciones(pedido.getObservaciones())
                                .productos(productos)
                                .createdAt(pedido.getCreatedAt())
                                .updatedAt(pedido.getUpdatedAt())
                                .build();
        }

        @Override
        @Transactional
        public PedidoDTO addProductoToPedido(Long pedidoId, PedidoProductoDTO productoDTO) {
                Pedido pedido = pedidoRepository.findById(pedidoId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Pedido not found with id: " + pedidoId));

                Producto producto = productoRepository.findById(productoDTO.getProductoId())
                                .orElseThrow(() -> new ResourceNotFoundException("Producto not found"));

                // Verificar stock
                if (producto.getStock() < productoDTO.getCantidad()) {
                        throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
                }

                // Actualizar stock
                producto.setStock(producto.getStock() - productoDTO.getCantidad());
                productoRepository.save(producto);

                // Crear relación pedido-producto
                PedidoProducto pp = PedidoProducto.builder()
                                .pedido(pedido)
                                .producto(producto)
                                .cantidad(productoDTO.getCantidad())
                                .precioUnitario(productoDTO.getPrecioUnitario())
                                .subtotal(productoDTO.getSubtotal())
                                .build();

                pedido.getProductos().add(pp);

                // Actualizar total del pedido
                pedido.setTotal(pedido.getTotal().add(pp.getSubtotal()));

                Pedido updated = pedidoRepository.save(pedido);
                return toDTO(updated);

        }

        @Override
        @Transactional
        public PedidoProductoDTO updatePedidoProducto(Long id, PedidoProductoDTO productoDTO) {
                PedidoProducto pp = pedidoProductoRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "PedidoProducto not found with id: " + id));

                Producto producto = pp.getProducto();
                int oldQty = pp.getCantidad();
                int newQty = productoDTO.getCantidad();
                int diff = newQty - oldQty;

                // Verificar stock
                if (diff > 0 && producto.getStock() < diff) {
                        throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
                }

                // Actualizar stock
                producto.setStock(producto.getStock() - diff);
                productoRepository.save(producto);

                // Actualizar PedidoProducto
                pp.setCantidad(newQty);
                // Recalculate subtotal based on unit price
                pp.setSubtotal(pp.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(newQty)));

                PedidoProducto updatedPP = pedidoProductoRepository.save(pp);

                // Recalculate parent Total
                recalculatePedidoTotal(pp.getPedido());

                return PedidoProductoDTO.builder()
                                .id(updatedPP.getId())
                                .pedidoId(updatedPP.getPedido().getId())
                                .productoId(updatedPP.getProducto().getId())
                                .productoNombre(updatedPP.getProducto().getNombre())
                                .cantidad(updatedPP.getCantidad())
                                .precioUnitario(updatedPP.getPrecioUnitario())
                                .subtotal(updatedPP.getSubtotal())
                                .build();
        }

        @Override
        @Transactional
        public void deletePedidoProducto(Long id) {
                PedidoProducto pp = pedidoProductoRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "PedidoProducto not found with id: " + id));

                // Restore stock
                Producto producto = pp.getProducto();
                producto.setStock(producto.getStock() + pp.getCantidad());
                productoRepository.save(producto);

                Pedido pedido = pp.getPedido();
                // Remove from list to ensure calculation is correct if we iterate
                pedido.getProductos().remove(pp);

                pedidoProductoRepository.delete(pp);

                // Recalculate parent Total
                recalculatePedidoTotal(pedido);
        }

        private void recalculatePedidoTotal(Pedido pedido) {
                java.math.BigDecimal total = java.math.BigDecimal.ZERO;
                if (pedido.getProductos() != null) {
                        for (PedidoProducto p : pedido.getProductos()) {
                                // Warning: if 'p' is the one we just deleted but still in memory list?
                                // We removed it from list above in delete method, so safe.
                                total = total.add(p.getSubtotal());
                        }
                }
                pedido.setTotal(total);
                pedidoRepository.save(pedido);
        }
}
