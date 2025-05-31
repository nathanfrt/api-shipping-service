package com.inter.shipping_service.controller;

import com.inter.shipping_service.dto.BalanceDto;
import com.inter.shipping_service.service.BalanceService;
import com.inter.shipping_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/balance")
@Tag(name = "Saldo Bancário", description = "Controlador para salvar e consultar saldo por usuário")
public class BalanceController {

    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);

    @Autowired
    private BalanceService balanceService;
    @Autowired
    private UserService userService;

    @GetMapping("/document")
    @Operation(summary = "Consultar saldo por documento")
    public ResponseEntity<?> getBalancebyDocumentNumber(@RequestParam String documentNumber) {
        try {
            if (!userService.existsUserByDocumentNumber(documentNumber)) {
                logger.warn("User not found");
                return ResponseEntity.notFound().build();
            }
            var balance = userService.getBalanceByDocumentNumber(documentNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(balance);
        }
        catch (Exception e){
            logger.error("Error fetching balance: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping()
    @Operation(summary = "Atualizar saldo BRL por documento")
    public ResponseEntity<?> putBalance(@RequestBody @Valid BalanceDto balanceDto){
        try{
            if (!userService.existsUserByDocumentNumber(balanceDto.documentNumber())){
                logger.warn("User not found");
                return ResponseEntity.notFound().build();
            }

            balanceService.updateBalance(balanceDto);
            return ResponseEntity.status(HttpStatus.OK).body("Value updated.");
        }
        catch (Exception e){
            logger.error("Error updating balance: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
