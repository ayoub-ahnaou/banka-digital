package com.digital.banka.service.implementation;

import com.digital.banka.dto.document.request.UploadDocumentRequest;
import com.digital.banka.dto.document.response.DocumentResponse;
import com.digital.banka.exception.ResourceNotFoundException;
import com.digital.banka.mapper.DocumentMapper;
import com.digital.banka.model.entity.Document;
import com.digital.banka.model.entity.Operation;
import com.digital.banka.repository.DocumentRepository;
import com.digital.banka.repository.OperationRepository;
import com.digital.banka.service.DocumentService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final OperationRepository operationRepository;
    private final DocumentMapper documentMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB in bytes

    @Override
    public DocumentResponse uploadDocument(UploadDocumentRequest request, Long operationId) {
        // 1. Validate file
        validateFile(request.getFile());

        // 2. Check if operation exists
        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new ResourceNotFoundException("Operation not found with id: " + operationId));

        // 3. Create unique filename
        String originalFilename = request.getFile().getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = Instant.now().toEpochMilli() + "_" + originalFilename;

        // 4. Create storage directory if not exists
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }

        // 5. Save file to disk
        Path filePath = uploadPath.resolve(uniqueFilename);
        try {
            Files.copy(request.getFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + e.getMessage(), e);
        }

        // 6. Create document record
        Document document = new Document();
        document.setFileName(originalFilename);
        document.setFileType(fileExtension);
        document.setStoragePath(filePath.toString());
        document.setOperation(operation);

        Document savedDocument = documentRepository.save(document);
        return documentMapper.toResponse(savedDocument);
    }

    // validate file helper method
    private void validateFile(MultipartFile file) {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new ValidationException("File is empty");
        }

        // Check file size (5MB limit)
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ValidationException(
                    String.format("File size exceeds 5MB limit. Current size: %.2f MB",
                            file.getSize() / (1024.0 * 1024.0))
            );
        }

        // Check file type (PDF, JPG, PNG)
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        if (contentType == null) {
            throw new ValidationException("Could not determine file type");
        }

        boolean isValidType = contentType.equals("application/pdf") ||
                contentType.startsWith("image/jpeg") ||
                contentType.startsWith("image/jpg") ||
                contentType.startsWith("image/png");

        if (!isValidType) {
            throw new ValidationException(
                    "Invalid file type. Only PDF, JPG, and PNG files are allowed"
            );
        }

        // Additional filename validation
        if (originalFilename == null || originalFilename.contains("..")) {
            throw new ValidationException("Invalid filename");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
