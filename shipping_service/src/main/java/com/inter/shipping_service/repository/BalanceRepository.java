package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, String> {
    Boolean existsByDocumentNumber(String documentNumber);


}
