package com.digital.banka.service.implementation;

import com.digital.banka.dto.auth.request.LoginRequest;
import com.digital.banka.dto.auth.request.RegisterRequest;
import com.digital.banka.dto.auth.response.LoginResponse;
import com.digital.banka.dto.auth.response.RegisterResponse;
import com.digital.banka.exception.DuplicateResourceException;
import com.digital.banka.exception.ResourceNotFoundException;
import com.digital.banka.mapper.UserMapper;
import com.digital.banka.model.entity.Account;
import com.digital.banka.model.entity.User;
import com.digital.banka.model.enums.Role;
import com.digital.banka.repository.AccountRepository;
import com.digital.banka.repository.UserRepository;
import com.digital.banka.security.JwtUtil;
import com.digital.banka.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

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

    @Override
    public LoginResponse login(LoginRequest request) {
        // if authentication fails, BadCredentialException will be thrown
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationServiceException("System error"));

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new DisabledException("User account is disabled");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String accessToken = jwtUtil.generateToken(userDetails);

        return new LoginResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt(),
                accessToken,
                jwtUtil.getJwtExpiration()
        );
    }

    @Override
    public void deactivateUserAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void activateUserAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setActive(true);
        userRepository.save(user);
    }
}
