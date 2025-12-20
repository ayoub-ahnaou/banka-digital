package com.digital.banka.service;

import com.digital.banka.dto.document.request.UploadDocumentRequest;
import com.digital.banka.dto.document.response.DocumentResponse;

public interface DocumentService {
    DocumentResponse uploadDocument(UploadDocumentRequest request, Long operationId);
}
