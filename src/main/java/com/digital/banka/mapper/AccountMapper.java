package com.digital.banka.mapper;

import com.digital.banka.dto.account.response.AccountResponse;
import com.digital.banka.model.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "owner.username", target = "username")
    @Mapping(source = "owner.email", target = "email")
    @Mapping(source = "owner.role", target = "role")
    @Mapping(source = "owner.active", target = "active")
    AccountResponse toResponse(Account account);
}
