package com.digital.banka.repository;

import com.digital.banka.model.entity.Account;
import com.digital.banka.model.entity.Operation;
import com.digital.banka.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findOperationByStatus(Status status);

    @Query("SELECT o FROM Operation o WHERE o.accountSource = :account")
    List<Operation> findByAccountSource(@Param("account") Account account);
}
