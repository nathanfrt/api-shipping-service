package com.inter.shipping_service.controller;

import com.inter.shipping_service.dto.TransactionDto;
import com.inter.shipping_service.exception.TransactionFail;
import com.inter.shipping_service.model.Transaction;
import com.inter.shipping_service.service.TransactionService;
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

@RestController
@RequestMapping("/transaction")
@Tag(name = "Transações/Transferências bancárias", description = "Controlador para obter e realizar transações entre usuários")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @GetMapping("/by")
    @Operation(summary = "Obter transações pelo usuário indicado")
    public ResponseEntity<?> getTransactionByDocumentNumber(@RequestParam @Valid String documentNumber){
        try {
            userService.exceptionDocumentNumber(documentNumber);

            var transaction = transactionService.getTransactionsByUser(documentNumber);
            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        }
        catch (Exception e){
            logger.error("Error fetching transactions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/to")
    @Operation(summary = "Obter transações para o usuário indicado")
    public ResponseEntity<?> getTransactionToDocumentNumber(@RequestParam @Valid String documentNumber){
        try {
            userService.exceptionDocumentNumber(documentNumber);

            var transaction = transactionService.getTransactionsToUser(documentNumber);
            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        }
        catch (Exception e){
            logger.error("Error fetching transactions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/real")
    @Operation(summary = "Transferir de BRL para BRL")
    public ResponseEntity<?> postTransactionRealToReal(@RequestBody @Valid TransactionDto transactionDto) {
        try {
            var transaction = transactionService.transactionBRLToBRL(transactionDto);

            if (transaction == null) {
                logger.warn("Transaction fail");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction fail");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        }
        catch (Exception e){
            logger.error("Error fetching transactions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/dollar")
    @Operation(summary = "Transferir de USD para USD")
    public ResponseEntity<?> postTransactionDollarToDollar(@RequestBody @Valid TransactionDto transactionDto) {
        try {
            var transaction = transactionService.transactionUSAToUSA(transactionDto);

            if (transaction == null) {
                logger.warn("Transaction fail");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction fail");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        }
        catch (Exception e) {
            logger.error("Error fetching transactions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/remessa")
    @Operation(summary = "Realizar remessa online", description = "Converte o valor da conta de transactionTo de BRL para USD e transfere o valor USD convertido")
    public ResponseEntity<?> postTransaction(@RequestBody @Valid TransactionDto transactionDto){
        try {
            Transaction transaction = transactionService.postRemessaService(transactionDto);

            if (transaction == null) {
                logger.warn("Transaction fail");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction fail");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        }
        catch (Exception e){
            logger.error("Error fetching transactions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
