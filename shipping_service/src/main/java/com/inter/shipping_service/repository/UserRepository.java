package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
