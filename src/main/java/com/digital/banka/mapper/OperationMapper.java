package com.digital.banka.mapper;

import com.digital.banka.dto.operation.request.DepositRequest;
import com.digital.banka.dto.operation.request.WithdrawRequest;
import com.digital.banka.dto.operation.response.OperationResponse;
import com.digital.banka.model.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OperationMapper {

    @Mapping(source = "accountSource.id", target = "accountSourceId")
    @Mapping(source = "accountSource.accountNumber", target = "accountSourceNumber")
    OperationResponse toResponse(Operation operation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.digital.banka.model.enums.Status.PENDING)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "validatedAt", ignore = true)
    @Mapping(target = "executedAt", ignore = true)
    @Mapping(target = "accountSource", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Operation toEntity(DepositRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.digital.banka.model.enums.Status.PENDING)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "validatedAt", ignore = true)
    @Mapping(target = "executedAt", ignore = true)
    @Mapping(target = "accountSource", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Operation toEntity(WithdrawRequest dto);
}
