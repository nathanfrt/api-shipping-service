package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Boolean existsByTransactionBy(String transactionBy);
    Boolean existsByTransactionTo(String transactionTo);

    List<Transaction> findAllByTransactionBy(String transactionBy);
    List<Transaction> findAllByTransactionTo(String transactionTo);

    @Query("SELECT COALESCE(SUM(t.limitUsed), 0) FROM Transaction t WHERE t.transactionBy = :documentNumber AND t.createdAt = :dateTransaction")
    Double limitDay(@Param("documentNumber") String documentNumber, @Param("dateTransaction") LocalDate dateTransaction);

}
