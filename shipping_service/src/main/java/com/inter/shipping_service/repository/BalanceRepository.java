package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Boolean existsTypeBalance(String typeBalance);
    Boolean existsByDocumentNumber(String documentNumber);

}
