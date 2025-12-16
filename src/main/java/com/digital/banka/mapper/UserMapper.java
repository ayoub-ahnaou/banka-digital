package com.digital.banka.mapper;

import com.digital.banka.dto.auth.response.RegisterResponse;
import com.digital.banka.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegisterResponse toRegisterReponse(User user);
}
