package com.inter.shipping_service.controller;

import com.inter.shipping_service.dto.TransactionDto;
import com.inter.shipping_service.exception.TransactionFail;
import com.inter.shipping_service.model.Transaction;
import com.inter.shipping_service.service.TransactionService;
import com.inter.shipping_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @GetMapping("/by")
    public ResponseEntity<?> getTransactionByDocumentNumber(@RequestParam @Valid String documentNumber){
        try {
            userService.exceptionDocumentNumber(documentNumber);

            var transaction = transactionService.getTransactionsByUser(documentNumber);
            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/to")
    public ResponseEntity<?> getTransactionToDocumentNumber(@RequestParam @Valid String documentNumber){
        try {
            userService.exceptionDocumentNumber(documentNumber);

            var transaction = transactionService.getTransactionsToUser(documentNumber);
            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/convert/real")
    public ResponseEntity<?> postTransactionRealToReal(@RequestBody @Valid TransactionDto transactionDto) {
        try {
            var transaction = transactionService.transactionBRLToBRL(transactionDto);

            if (transaction == null) {
                throw new TransactionFail("Transaction fail");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/convert/dollar")
    public ResponseEntity<?> postTransactionDollarToDollar(@RequestBody @Valid TransactionDto transactionDto) {
        try {
            var transaction = transactionService.transactionUSAToUSA(transactionDto);

            if (transaction == null) {
                throw new TransactionFail("Transaction fail");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/convert/remessa")
    public ResponseEntity<?> postTransaction(@RequestBody @Valid TransactionDto transactionDto){
        try {
            Transaction transaction = transactionService.postRemessaService(transactionDto);

            if (transaction == null) {
                throw new TransactionFail("Transaction fail");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
