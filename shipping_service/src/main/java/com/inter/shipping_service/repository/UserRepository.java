package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.Balance;
import com.inter.shipping_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByDocumentNumber(String documentNumber);
}
