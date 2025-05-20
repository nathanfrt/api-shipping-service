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
    @Autowired
    private UserService userService;

    @GetMapping("/document")
    public ResponseEntity<?> getBalancebyDocumentNumber(@RequestParam String documentNumber) {
        try {
            if (!userService.existsUserByDocumentNumber(documentNumber)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Users not found");
            }
            var balance = userService.getBalanceByDocumentNumber(documentNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(balance);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<?> putBalance(@RequestBody @Valid BalanceDto balanceDto){
        try{
        if (!userService.existsUserByDocumentNumber(balanceDto.documentNumber())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
        }

        balanceService.updateBalance(balanceDto);
        return ResponseEntity.status(HttpStatus.OK).body("Value updated.");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
