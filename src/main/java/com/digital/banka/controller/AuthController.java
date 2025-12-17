package com.digital.banka.controller;

import com.digital.banka.dto.ApiResponse;
import com.digital.banka.dto.ApiResponseSuccess;
import com.digital.banka.dto.auth.request.LoginRequest;
import com.digital.banka.dto.auth.request.RegisterRequest;
import com.digital.banka.dto.auth.response.LoginResponse;
import com.digital.banka.dto.auth.response.RegisterResponse;
import com.digital.banka.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse user = userService.createUser(request);
        ApiResponseSuccess<RegisterResponse> res = new ApiResponseSuccess<>(201, "User registered successfully", user);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginReponse = userService.login(request);
        ApiResponseSuccess<LoginResponse> res = new ApiResponseSuccess<>(200, "User logged in successfully", loginReponse);

        return ResponseEntity.ok(res);
    }
}
