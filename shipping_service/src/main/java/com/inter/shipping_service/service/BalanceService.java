package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.BalanceDto;
import com.inter.shipping_service.model.*;
import com.inter.shipping_service.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Verifica se existe tipo de moeda
    public boolean existsTypeBalance(String typeBalance){
        return balanceRepository.existsTypeBalance(typeBalance);
    }

    // Atualizar saldo
    public String updateBalance(BalanceDto balanceDto){
        if (!userService.existsUserByDocumentNumber(balanceDto.documentNumber()))
            return "not exist";

        var user = userService.getUserByDocumentNumber(balanceDto.documentNumber());
        Double balance = 0.0;

        if (balanceDto.typeBalance() == TypeBalance.BR) {
            balance += balanceDto.amount();
            user.setBalanceReal(balance);
        } else if (balanceDto.typeBalance() == TypeBalance.USD) {
            balance += balanceDto.amount();
            user.setBalanceDolar(balance);
        }
        return "success";
    }

}
