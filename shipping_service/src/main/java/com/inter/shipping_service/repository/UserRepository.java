package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.BalanceResponse;
import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
        Boolean existsByDocumentNumber(String documentNumber);
    Boolean existsById(long id);
    Boolean existsByEmail(String email);

    Double findBalanceDolarByDocumentNumber(String documentNumber);
    Double findBalanceRealByDocumentNumber(String documentNumber);

    User findUserByDocumentNumber(String documentNumber);
    TypeUser findTypeUserByDocumentNumber(String documentNumber);

    @Query("SELECT new BalanceResponse(u.id, u.balanceReal, u.balanceDollar) FROM User u WHERE u.documentNumber = :documentNumber")
    BalanceResponse findBalanceByDocumentNumber(@Param("documentNumber") String documentNumber);


}
