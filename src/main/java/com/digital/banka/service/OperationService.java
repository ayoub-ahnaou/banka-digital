package com.digital.banka.service;

import com.digital.banka.dto.operation.request.DepositRequest;
import com.digital.banka.dto.operation.response.OperationResponse;

public interface OperationService {
    OperationResponse deposit(DepositRequest request);
}
