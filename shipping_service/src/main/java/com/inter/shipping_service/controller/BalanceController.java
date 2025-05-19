package com.inter.shipping_service.controller;

import com.inter.shipping_service.dto.BalanceDto;
import com.inter.shipping_service.service.BalanceService;
import com.inter.shipping_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;
    private UserService userService;

    @GetMapping("/document/")
    public ResponseEntity<?> getBalancebyDocumentNumber(@RequestParam String documentNumber) {
        if (!userService.existsUserByDocumentNumber(documentNumber)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Users not found");
        }
        var balance = userService.getBalanceByDocumentNumber(documentNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(balance);
    }

    @PutMapping()
    public ResponseEntity<Object> putBalance(@RequestParam @Valid BalanceDto balanceDto){
        if (!userService.existsUserByDocumentNumber(balanceDto.documentNumber())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
        }
        if (!balanceService.existsTypeBalance(balanceDto.typeBalance().toString())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Type of balance not found.");
        }

        var user = userService.getUserByDocumentNumber(balanceDto.documentNumber());
        balanceService.updateBalance(balanceDto);

        return ResponseEntity.status(HttpStatus.OK).body("Value updated.");
    }

}
