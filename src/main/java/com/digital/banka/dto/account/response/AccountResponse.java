package com.digital.banka.dto.account.response;

import com.digital.banka.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    private Long id;
    private String username;
    private String email;

    private Role role;
    private String accountNumber;
    private Double balance;
}
