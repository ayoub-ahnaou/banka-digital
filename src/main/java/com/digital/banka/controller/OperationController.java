package com.digital.banka.controller;

import com.digital.banka.dto.ApiResponse;
import com.digital.banka.dto.ApiResponseSuccess;
import com.digital.banka.dto.operation.request.DepositRequest;
import com.digital.banka.dto.operation.response.OperationResponse;
import com.digital.banka.model.enums.Status;
import com.digital.banka.service.OperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                getDepositMessage(response.getStatus(), request.getAmount()),
                response
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    private String getDepositMessage(Status status, Double amount) {
        if (status == Status.APPROVED) {
            return String.format("Deposit of %.2f DH approved successfully", amount);
        } else if (status == Status.PENDING) {
            return String.format("Deposit of %.2f DH requires agent approval, give use some additional document so we can approuve your operation", amount);
        }
        return "Deposit created";
    }
}
