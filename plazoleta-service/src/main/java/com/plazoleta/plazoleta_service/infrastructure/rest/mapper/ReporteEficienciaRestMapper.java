package com.plazoleta.plazoleta_service.infrastructure.rest.mapper;

import com.plazoleta.plazoleta_service.domain.model.ReporteEficienciaPedidos;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.ReporteEficienciaResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReporteEficienciaRestMapper {

    ReporteEficienciaResponseDTO toResponse(ReporteEficienciaPedidos reporte);
}
