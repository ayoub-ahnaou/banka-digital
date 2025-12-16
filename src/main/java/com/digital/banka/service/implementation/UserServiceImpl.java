package com.digital.banka.service.implementation;

import com.digital.banka.dto.auth.request.RegisterRequest;
import com.digital.banka.dto.auth.response.RegisterResponse;
import com.digital.banka.exception.DuplicateResourceException;
import com.digital.banka.mapper.UserMapper;
import com.digital.banka.model.entity.Account;
import com.digital.banka.model.entity.User;
import com.digital.banka.model.enums.Role;
import com.digital.banka.repository.AccountRepository;
import com.digital.banka.repository.UserRepository;
import com.digital.banka.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Email or Username already exist: " + request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        user.setRole(Role.CLIENT);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // create account automatically
        Account account = new Account();
        account.setOwner(savedUser);
        account.setBalance(0.0);
        account.getOwner().setActive(true);

        Account savedAccount = accountRepository.save(account);
        savedUser.setAccount(savedAccount);

        // save user
        userRepository.save(savedUser);

        return userMapper.toRegisterReponse(savedUser);
    }
}
