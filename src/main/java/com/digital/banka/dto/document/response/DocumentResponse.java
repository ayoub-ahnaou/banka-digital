package com.digital.banka.dto.document.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {

    private Long id;
    private String fileName;
    private String fileType;
    private String storagePath;
    private LocalDateTime createdAt;
    private Long operationId;
}
