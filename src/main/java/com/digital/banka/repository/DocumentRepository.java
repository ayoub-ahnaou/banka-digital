package com.digital.banka.repository;

import com.digital.banka.model.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByOperationId(Long operationId);
}
