package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.BalanceDto;
import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.model.Balance;
import com.inter.shipping_service.model.TypeBalance;
import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.BalanceRepository;
import com.inter.shipping_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private UserRepository userRepository;

    // Buscar saldos por Documento
    public User getBalanceByDocument(@PathVariable String documentNumber){
        return userRepository.findByDocumentNumber(documentNumber).orElse(null);
    }

    // Atualizar saldo
    public ResponseEntity<String> updateBalance(@RequestBody BalanceDto balanceDto){
        var user = userRepository.findByDocumentNumber(balanceDto.documentNumber());

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Balance newBalance = new Balance(balanceDto);

        if (balanceDto.typeBalance() == TypeBalance.BR) {
            user.ifPresent(existingUser -> {
                double updatedBalance = existingUser.getBalanceReal() != null ? existingUser.getBalanceReal() : 0.0;
                updatedBalance += balanceDto.amount();
                existingUser.setBalanceReal(updatedBalance);
                userRepository.save(existingUser);
            });
        } else if (balanceDto.typeBalance() == TypeBalance.USD) {
            user.ifPresent(existingUser -> {
                double updatedBalance = existingUser.getBalanceDolar() != null ? existingUser.getBalanceDolar() : 0.0;
                updatedBalance += balanceDto.amount();
                existingUser.setBalanceDolar(updatedBalance);
                userRepository.save(existingUser);
            });
        }

        balanceRepository.save(newBalance);
        return ResponseEntity.status(HttpStatus.CREATED).body("Balance updated successfully");
    }

}
