package com.digital.banka.controller;

import com.digital.banka.dto.ApiResponse;
import com.digital.banka.dto.ApiResponseSuccess;
import com.digital.banka.dto.operation.request.DepositRequest;
import com.digital.banka.dto.operation.request.TransferRequest;
import com.digital.banka.dto.operation.request.WithdrawRequest;
import com.digital.banka.dto.operation.response.OperationResponse;
import com.digital.banka.model.enums.Status;
import com.digital.banka.model.enums.Type;
import com.digital.banka.service.OperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse> depositForCurrentUser(@RequestBody @Valid DepositRequest request) {

        OperationResponse response = operationService.deposit(request);

        ApiResponseSuccess<OperationResponse> apiResponse = new ApiResponseSuccess<>(
                HttpStatus.CREATED.value(),
                getDepositMessage(response.getType(), response.getStatus(), request.getAmount()),
                response
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse> withdrawForCurrentUser(@RequestBody @Valid WithdrawRequest request) {

        OperationResponse response = operationService.withdraw(request);

        ApiResponseSuccess<OperationResponse> apiResponse = new ApiResponseSuccess<>(
                HttpStatus.CREATED.value(),
                getDepositMessage(response.getType(), response.getStatus(), request.getAmount()),
                response
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transferForCurrentUser(@RequestBody @Valid TransferRequest request) {
        OperationResponse response = operationService.transfer(request);

        ApiResponseSuccess<OperationResponse> apiResponse = new ApiResponseSuccess<>(
                HttpStatus.CREATED.value(),
                getDepositMessage(response.getType(), response.getStatus(), request.getAmount()),
                response
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    private String getDepositMessage(Type type, Status status, Double amount) {
        if (status == Status.APPROVED) {
            return String.format(type + " of %.2f DH approved successfully", amount);
        } else if (status == Status.PENDING) {
            return String.format(type + " of %.2f DH requires agent approval, give use some additional document so we can approuve your operation", amount);
        }
        return type + " created";
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BANK_AGENT')")
    public ResponseEntity<ApiResponse> getOperationsByStatus(@RequestParam(required = false) Status status) {
        List<OperationResponse> operations = status != null
                ? operationService.getOperationsByStatus(status)
                : operationService.getAllOperations();

        ApiResponseSuccess<Object> apiResponse = new ApiResponseSuccess<>(
                HttpStatus.OK.value(),
                "Operations retrieved successfully",
                operations
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
