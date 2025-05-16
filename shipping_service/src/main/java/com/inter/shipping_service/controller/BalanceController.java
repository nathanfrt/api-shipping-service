package com.inter.shipping_service.controller;

import com.inter.shipping_service.dto.BalanceDto;
import com.inter.shipping_service.model.Balance;
import com.inter.shipping_service.repository.BalanceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private BalanceRepository balanceRepository;

    @PutMapping("/balance/{id}/{typeBalance}")
    public ResponseEntity<Object> putBalance(@PathVariable(value = "id") long id,
                                              @Valid Balanc balanceDto){

        Optional<Balance> existId = balanceRepository.findById(id);
        if(existId.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        var balance = new Balance();
        BeanUtils.copyProperties(B);


    }

    @GetMapping("/balance/{id}/{typeBalance}")
    public void balance(){
        balanceRepository.

    }

}
