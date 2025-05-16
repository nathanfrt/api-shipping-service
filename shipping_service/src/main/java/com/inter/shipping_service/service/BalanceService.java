package com.inter.shipping_service.service;

import com.inter.shipping_service.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    public void (@RequestParam long id, @RequestParam String typeBalance){
        balanceRepository.findAll()

    }
}
