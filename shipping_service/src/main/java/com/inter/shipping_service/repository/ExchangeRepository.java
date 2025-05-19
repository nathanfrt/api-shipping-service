package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExchangeRepository extends JpaRepository<Exchange, UUID> {
}
