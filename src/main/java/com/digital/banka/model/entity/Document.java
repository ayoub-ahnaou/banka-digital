package com.digital.banka.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private String storagePath;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "operation_id")
    private Operation operation;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
