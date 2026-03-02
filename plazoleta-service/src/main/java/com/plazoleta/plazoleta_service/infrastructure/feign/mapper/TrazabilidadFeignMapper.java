package com.plazoleta.plazoleta_service.infrastructure.feign.mapper;

import com.plazoleta.plazoleta_service.domain.model.TrazabilidadEvento;
import com.plazoleta.plazoleta_service.infrastructure.feign.dto.TrazabilidadResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrazabilidadFeignMapper {

    TrazabilidadEvento toDomain(TrazabilidadResponse response);

    List<TrazabilidadEvento> toDomainList(List<TrazabilidadResponse> response);
}
