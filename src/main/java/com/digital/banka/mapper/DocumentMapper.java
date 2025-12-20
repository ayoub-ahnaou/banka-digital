package com.digital.banka.mapper;

import com.digital.banka.dto.document.response.DocumentResponse;
import com.digital.banka.model.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(source = "operation.id", target = "operationId")
    DocumentResponse toResponse(Document document);
}
