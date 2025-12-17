package com.app.salesmasterpro.services;

import com.app.salesmasterpro.dtos.PedidoDTO;
import com.app.salesmasterpro.dtos.PedidoProductoDTO;

import java.util.List;

public interface PedidoService {
    List<PedidoDTO> findAll();

    PedidoDTO findById(Long id);

    PedidoDTO create(PedidoDTO pedidoDTO);

    PedidoDTO update(Long id, PedidoDTO pedidoDTO);

    void delete(Long id);

    PedidoDTO addProductoToPedido(Long pedidoId, PedidoProductoDTO productoDTO);

    PedidoProductoDTO updatePedidoProducto(Long id, PedidoProductoDTO productoDTO);

    void deletePedidoProducto(Long id);
}
