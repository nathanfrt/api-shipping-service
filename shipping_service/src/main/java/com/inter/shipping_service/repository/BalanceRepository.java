package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance>findByDocumentNumber(String documentNumber);

    Balance getBalanceByDocumentNumber(String documentNumber);
}
