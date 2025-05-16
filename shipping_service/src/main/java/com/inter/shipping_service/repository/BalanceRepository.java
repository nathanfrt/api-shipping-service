package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance getBalanceById(long id);


    Long id(Long id);
}
