package com.digital.banka.service.implementation;

import com.digital.banka.dto.account.response.AccountResponse;
import com.digital.banka.exception.ResourceNotFoundException;
import com.digital.banka.mapper.AccountMapper;
import com.digital.banka.model.entity.Account;
import com.digital.banka.model.entity.User;
import com.digital.banka.repository.AccountRepository;
import com.digital.banka.repository.UserRepository;
import com.digital.banka.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponse getAccountByCurrentUser() {
        String username = getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getAccount() == null)
            throw new ResourceNotFoundException("User have no account");

        Account account = accountRepository.findById(user.getAccount().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return accountMapper.toResponse(account);
    }

    @NotNull
    private static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Aucun utilisateur connecté");
        }

        String username;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new ResourceNotFoundException("Impossible de récupérer les informations de l'utilisateur");
        }
        return username;
    }
}
