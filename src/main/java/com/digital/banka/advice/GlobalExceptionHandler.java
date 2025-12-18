package com.digital.banka.advice;

import com.digital.banka.dto.ApiResponse;
import com.digital.banka.dto.ApiResponseError;
import com.digital.banka.exception.InsufficientBalanceException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ApiResponseError errorResponse = new ApiResponseError(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication Failed",
                List.of(ex.getMessage())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponseError> handleInsufficientBalance(InsufficientBalanceException ex) {
        ApiResponseError errorResponse = new ApiResponseError(
                HttpStatus.BAD_REQUEST.value(),
                "Insufficient Balance",
                List.of(ex.getMessage())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + ": " + errorMessage);
        });

        ApiResponseError errorResponse = new ApiResponseError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseError> handleAccessDeniedException(
            org.springframework.security.access.AccessDeniedException ex) {

        ApiResponseError errorResponse = new ApiResponseError(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                List.of("You don't have permission to access this resource.")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponseError> handle401(AuthenticationException ex) {
        String message = ex instanceof BadCredentialsException
                ? "Invalid username or password"
                : "Authentication required. Please login.";

        return ResponseEntity.status(401).body(
                new ApiResponseError(401, "Authentication Failed", List.of(message))
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponseError errorResponse = new ApiResponseError(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Request",
                List.of(ex.getMessage())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiResponseError> handleSecurityException(SecurityException ex) {
        ApiResponseError errorResponse = new ApiResponseError(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                List.of(ex.getMessage())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
