package com.digital.banka.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    private Double balance;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private User owner;

    @OneToMany(mappedBy = "accountSource", cascade = CascadeType.ALL)
    private List<Operation> operations;

    @PrePersist
    protected void onCreate() {
        if (accountNumber == null) {
            accountNumber = "BAR-" +
                    UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        }
    }
}
