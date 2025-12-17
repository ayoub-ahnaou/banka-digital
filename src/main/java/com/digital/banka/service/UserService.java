package com.digital.banka.service;

import com.digital.banka.dto.auth.request.LoginRequest;
import com.digital.banka.dto.auth.request.RegisterRequest;
import com.digital.banka.dto.auth.response.LoginResponse;
import com.digital.banka.dto.auth.response.RegisterResponse;

public interface UserService {
    RegisterResponse createUser(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
