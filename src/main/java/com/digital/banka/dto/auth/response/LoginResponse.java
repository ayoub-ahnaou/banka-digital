package com.digital.banka.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;

    private String accessToken;
    private final String tokenType = "Bearer";
    private Long expiresIn;
}
