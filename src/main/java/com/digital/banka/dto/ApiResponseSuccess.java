package com.digital.banka.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApiResponseSuccess<T> extends ApiResponse {
    private T data;

    public ApiResponseSuccess(Integer status, String message, T data) {
        super(status, message, LocalDateTime.now());
        this.data = data;
    }
}
