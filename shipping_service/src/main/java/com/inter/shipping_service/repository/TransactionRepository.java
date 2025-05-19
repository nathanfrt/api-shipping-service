package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Boolean existsTransactionByDocumentNumber(String documentNumber);
    Boolean existsTransactionToDocumentNumber(String documentNumber);

    List<Transaction> findAllTransactionByDocumentNumber(String documentNumber);
    List<Transaction> findAllTransactionToDocumentNumber(String documentNumber);
}
