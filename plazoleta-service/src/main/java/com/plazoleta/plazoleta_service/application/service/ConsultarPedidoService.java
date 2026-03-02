package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.exception.UsuarioNoAsociadoRestauranteException;
import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Pedido;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarPedidoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.IPedidoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IUsuarioClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultarPedidoService implements IConsultarPedidoUseCase {

    private final IPedidoRepositoryPort pedidoRepositoryPort;
    private final IUsuarioClientPort usuarioClientPort;

    @Override
    public PaginacionResultado<Pedido> listarPedidosPorEstado(Integer idEmpleado, EstadoPedidoEnum estado, int page, int size) {
        Long idRestaurante = usuarioClientPort.consultarIdRestauranteDeUsuario(idEmpleado);
        if (idRestaurante == null) {
            throw new UsuarioNoAsociadoRestauranteException();
        }
        return pedidoRepositoryPort.listarPorRestauranteYEstado(idRestaurante, estado, page, size);
    }
}
