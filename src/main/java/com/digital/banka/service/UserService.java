package com.digital.banka.service;

import com.digital.banka.dto.account.response.AccountResponse;
import com.digital.banka.dto.auth.request.LoginRequest;
import com.digital.banka.dto.auth.request.RegisterRequest;
import com.digital.banka.dto.auth.response.LoginResponse;
import com.digital.banka.dto.auth.response.RegisterResponse;
import com.digital.banka.model.entity.User;

import java.util.List;

public interface UserService {
    RegisterResponse createUser(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    void deactivateUserAccount(Long id);
    void activateUserAccount(Long id);

    void promoteUserToBankAgent(Long id);
    List<AccountResponse> getAllUsers();
}
