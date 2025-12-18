package com.digital.banka.dto.operation.response;

import com.digital.banka.model.enums.Status;
import com.digital.banka.model.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationResponse {

    private String accountSourceNumber;
    private Type type;
    private Double amount;
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime validatedAt;
    private LocalDateTime executedAt;

    private Long accountSourceId;
    private Long accountDestinationId;
}
