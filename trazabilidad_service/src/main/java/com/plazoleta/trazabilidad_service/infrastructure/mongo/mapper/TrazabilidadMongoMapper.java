package com.plazoleta.trazabilidad_service.infrastructure.mongo.mapper;

import com.plazoleta.trazabilidad_service.domain.model.Trazabilidad;
import com.plazoleta.trazabilidad_service.infrastructure.mongo.document.TrazabilidadDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrazabilidadMongoMapper {

    TrazabilidadDocument toDocument(Trazabilidad model);

    Trazabilidad toModel(TrazabilidadDocument document);

    List<Trazabilidad> toModelList(List<TrazabilidadDocument> documents);
}
