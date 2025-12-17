package com.digital.banka.service;

import com.digital.banka.dto.account.response.AccountResponse;

public interface AccountService {
    AccountResponse getAccountByCurrentUser();
}
