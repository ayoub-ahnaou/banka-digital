package com.digital.banka.repository;

import com.digital.banka.model.entity.Operation;
import com.digital.banka.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findOperationByStatus(Status status);
}
