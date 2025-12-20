package com.digital.banka.controller.admin;

import com.digital.banka.dto.ApiResponse;
import com.digital.banka.dto.ApiResponseSuccess;
import com.digital.banka.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivateUserAccount(@PathVariable("id") Long id) {
        userService.deactivateUserAccount(id);
        ApiResponseSuccess<Object> res = new ApiResponseSuccess<>(200, "User account deactivated successfully", null);
        return ResponseEntity.ok().body(res);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse> activateUserAccount(@PathVariable("id") Long id) {
        userService.activateUserAccount(id);
        ApiResponseSuccess<Object> res = new ApiResponseSuccess<>(200, "User account activated successfully", null);
        return ResponseEntity.ok().body(res);
    }

    @PatchMapping("/{id}/promote")
    public ResponseEntity<ApiResponse> promoteUserToBankAgent(@PathVariable("id") Long id) {
        userService.promoteUserToBankAgent(id);
        ApiResponseSuccess<Object> res = new ApiResponseSuccess<>(200, "User promoted to bank agent successfully", null);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        var users = userService.getAllUsers();
        ApiResponseSuccess<Object> res = new ApiResponseSuccess<>(200, "Users retrieved successfully", users);
        return ResponseEntity.ok().body(res);
    }
}
