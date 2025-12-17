package com.digital.banka.controller;

import com.digital.banka.dto.ApiResponse;
import com.digital.banka.dto.ApiResponseSuccess;
import com.digital.banka.dto.account.response.AccountResponse;
import com.digital.banka.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account")
    public ResponseEntity<ApiResponse> getCurrentUserAccount() {
        AccountResponse accountResponse = accountService.getAccountByCurrentUser();
        ApiResponseSuccess<AccountResponse> res = new ApiResponseSuccess<>(200, "Account retrieved successfully", accountResponse);

        return ResponseEntity.ok(res);
    }
}
