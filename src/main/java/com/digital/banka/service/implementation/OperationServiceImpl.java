package com.digital.banka.service.implementation;

import com.digital.banka.dto.operation.request.DepositRequest;
import com.digital.banka.dto.operation.request.WithdrawRequest;
import com.digital.banka.dto.operation.response.OperationResponse;
import com.digital.banka.exception.InsufficientBalanceException;
import com.digital.banka.exception.ResourceNotFoundException;
import com.digital.banka.mapper.OperationMapper;
import com.digital.banka.model.entity.Account;
import com.digital.banka.model.entity.Operation;
import com.digital.banka.model.entity.User;
import com.digital.banka.model.enums.Status;
import com.digital.banka.model.enums.Type;
import com.digital.banka.repository.AccountRepository;
import com.digital.banka.repository.OperationRepository;
import com.digital.banka.repository.UserRepository;
import com.digital.banka.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private static final Double SEUIL_AUTOMATIQUE = 10000.0;

    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final OperationMapper operationMapper;

    @Override
    public OperationResponse deposit(DepositRequest request) {
        Account account = getCurrentUserAccount();

        Operation operation = operationMapper.toEntity(request);
        operation.setType(Type.DEPOSIT);
        operation.setAccountSource(account);

        applyDepositOrWithdrawalRules(operation);

        if (operation.getStatus() == Status.APPROVED) {
            account.setBalance(account.getBalance() + operation.getAmount());
            accountRepository.save(account);
        }

        Operation savedOperation = operationRepository.save(operation);
        return operationMapper.toResponse(savedOperation);
    }

    @Override
    public OperationResponse withdraw(WithdrawRequest request) {
        Account account = getCurrentUserAccount();

        Operation operation = operationMapper.toEntity(request);
        operation.setType(Type.WITHDRAWAL);
        operation.setAccountSource(account);

        if (account.getBalance() < operation.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " + account.getBalance());
        }

        applyDepositOrWithdrawalRules(operation);

        if (operation.getStatus() == Status.APPROVED) {
            account.setBalance(account.getBalance() - operation.getAmount());
            accountRepository.save(account);
        }

        Operation savedOperation = operationRepository.save(operation);
        return operationMapper.toResponse(savedOperation);
    }

    private Account getCurrentUserAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getAccount() == null)
            throw new ResourceNotFoundException("User has no associated account");

        return user.getAccount();
    }

    private void applyDepositOrWithdrawalRules(Operation operation) {
        if (operation.getAmount() <= SEUIL_AUTOMATIQUE) {
            operation.setStatus(Status.APPROVED);
            operation.setExecutedAt(LocalDateTime.now());
            operation.setValidatedAt(LocalDateTime.now());
        } else {
            operation.setStatus(Status.PENDING);
            // TODO: throw an exception to tell user that operation needs manual approval (upload documents, etc.)
        }
    }
}
