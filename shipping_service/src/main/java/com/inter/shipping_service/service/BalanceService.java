package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.BalanceDto;
import com.inter.shipping_service.exception.InvalidDocument;
import com.inter.shipping_service.model.*;
import com.inter.shipping_service.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserService userService;

    // Buscar saldos por Documento
    public BalanceResponse getBalanceByDocument(String documentNumber){
        return userService.getBalanceByDocumentNumber(documentNumber);
    }

    // Verifica se existe transações por documento
    public Boolean existsBalanceByDocumentNumber(String documentNumber){
        return balanceRepository.existsByDocumentNumber(documentNumber);
    }

    // Atualizar saldo em BRL
    public void updateBalance(BalanceDto balanceDto){
        if (!userService.existsUserByDocumentNumber(balanceDto.documentNumber()))
            throw new InvalidDocument("Invalid document number");;

        var user = userService.getUserByDocumentNumber(balanceDto.documentNumber());

        Double balance = user.getBalanceReal() + balanceDto.amount();
        user.setBalanceReal(balance);
        userService.save(user);

        Balance newBalance = new Balance(balanceDto);
        newBalance.setCreatedAt(LocalDateTime.now());
        balanceRepository.save(newBalance);
    }
}
