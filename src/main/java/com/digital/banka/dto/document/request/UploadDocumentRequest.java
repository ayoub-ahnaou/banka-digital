package com.digital.banka.dto.document.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadDocumentRequest {

    @NotNull(message = "File is required")
    private MultipartFile file;
}
