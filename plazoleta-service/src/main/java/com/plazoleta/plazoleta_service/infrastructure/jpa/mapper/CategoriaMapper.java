package com.plazoleta.plazoleta_service.infrastructure.jpa.mapper;

import com.plazoleta.plazoleta_service.domain.model.Categoria;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.CategoriaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    Categoria toDomain(CategoriaEntity categoriaEntity);

    CategoriaEntity toEntity(Categoria categoria);

}
