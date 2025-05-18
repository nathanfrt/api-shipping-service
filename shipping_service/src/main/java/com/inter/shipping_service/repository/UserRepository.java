package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.BalanceResponse;
import com.inter.shipping_service.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.balanceDolar = :balanceDolar WHERE u.documentNumber = :documentNumber")
    void saveBalanceDolar (@Param("balanceDolar") Double balanceDolar, @Param("documentNumber") String documentNumber);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.balanceReal = :balanceReal WHERE u.documentNumber = :documentNumber")
    void saveBalanceReal (@Param("balanceReal") Double balanceReal,@Param("documentNumber") String documentNumber);

    Boolean existsByDocumentNumber(String documentNumber);
    Boolean existsById(long id);
    Boolean existsByEmail(String email);

    User findUserByDocumentNumber(String documentNumber);

    BalanceResponse findBalanceByDocumentNumber(String documentNumber);


}
