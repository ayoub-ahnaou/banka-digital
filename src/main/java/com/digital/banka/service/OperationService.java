package com.digital.banka.service;

import com.digital.banka.dto.operation.request.DepositRequest;
import com.digital.banka.dto.operation.request.TransferRequest;
import com.digital.banka.dto.operation.request.WithdrawRequest;
import com.digital.banka.dto.operation.response.OperationResponse;
import com.digital.banka.model.enums.Status;

import java.util.List;

public interface OperationService {
    OperationResponse deposit(DepositRequest request);
    OperationResponse withdraw(WithdrawRequest request);
    OperationResponse transfer(TransferRequest request);
    List<OperationResponse> getOperationsByStatus(Status status);
    List<OperationResponse> getAllOperations();
}
