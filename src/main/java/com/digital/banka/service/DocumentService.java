package com.digital.banka.service;

import com.digital.banka.dto.document.request.UploadDocumentRequest;
import com.digital.banka.dto.document.response.DocumentResponse;

import java.util.List;

public interface DocumentService {
    DocumentResponse uploadDocument(UploadDocumentRequest request, Long operationId);
    List<DocumentResponse> getDocumentsByOrderId(Long operationId);
}
