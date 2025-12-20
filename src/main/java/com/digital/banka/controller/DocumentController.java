package com.digital.banka.controller;

import com.digital.banka.dto.ApiResponse;
import com.digital.banka.dto.ApiResponseSuccess;
import com.digital.banka.dto.document.request.UploadDocumentRequest;
import com.digital.banka.dto.document.response.DocumentResponse;
import com.digital.banka.service.DocumentService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload/{operationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadDocument(
            @ModelAttribute @RequestBody UploadDocumentRequest request,
            @PathVariable("operationId") Long operationId) {

        // Manually validate
        if (request.getFile() == null || request.getFile().isEmpty()) {
            throw new ValidationException("File is required");
        }

        DocumentResponse response = documentService.uploadDocument(request, operationId);

        ApiResponseSuccess<DocumentResponse> apiResponse = new ApiResponseSuccess<>(
                HttpStatus.CREATED.value(),
                "Document uploaded successfully, our agents will review it shortly to approuve your operation.",
                response
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getDocumentsByOrderId(@PathVariable("orderId") Long orderId) {
        List<DocumentResponse> documents = documentService.getDocumentsByOrderId(orderId);
        ApiResponseSuccess<List<DocumentResponse>> apiResponse = new ApiResponseSuccess<>(
                HttpStatus.OK.value(),
                "Documents retrieved successfully",
                documents
        );
        return ResponseEntity.ok(apiResponse);
    }
}
