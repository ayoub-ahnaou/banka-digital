package com.digital.banka.mapper;

import com.digital.banka.dto.account.response.AccountResponse;
import com.digital.banka.model.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "owner.username", target = "username")
    @Mapping(source = "owner.email", target = "email")
    AccountResponse toResponse(Account account);
}
