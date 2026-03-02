package com.plazoleta.plazoleta_service.domain.ports.in;

import com.plazoleta.plazoleta_service.domain.model.FiltroEficienciaPedidos;
import com.plazoleta.plazoleta_service.domain.model.ReporteEficienciaPedidos;

public interface IConsultarEficienciaPedidosUseCase {

    ReporteEficienciaPedidos consultar(Integer idPropietario, FiltroEficienciaPedidos filtro);
}
