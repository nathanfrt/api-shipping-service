package com.inter.shipping_service.repository;

import com.inter.shipping_service.dto.BalanceResponseDto;
import com.inter.shipping_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByDocumentNumber(String documentNumber);
    Boolean existsById(long id);
    Boolean existsByEmail(String email);

    @Query("SELECT u.balanceReal FROM User u WHERE u.documentNumber = :documentNumber")
    Double findBalanceRealByDocumentNumber(String documentNumber);

    @Query("SELECT u.balanceDollar FROM User u WHERE u.documentNumber = :documentNumber")
    Double findBalanceDollarByDocumentNumber(String documentNumber);

    User findUserByDocumentNumber(String documentNumber);

    @Query("SELECT new com.inter.shipping_service.dto.BalanceResponseDto(u.balanceReal, u.balanceDollar) FROM User u WHERE u.documentNumber = :documentNumber")
    Optional<BalanceResponseDto> findBalanceByDocumentNumber(@Param("documentNumber") String documentNumber);

}
