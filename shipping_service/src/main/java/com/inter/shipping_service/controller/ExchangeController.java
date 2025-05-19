package com.inter.shipping_service.controller;

import com.inter.shipping_service.service.ExchangeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/quote")
    public ResponseEntity<?> olindaAPI_External(@RequestParam String date){
        try {
            double result = exchangeService.getQuote_External(date);
            if (result > 0){
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quote not found");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when checking quote: " + e.getMessage());
        }
    }

    @PostMapping("/convert/realToDollar")
    public ResponseEntity<?> postTransaction(@RequestParam @Valid String documentNumber, @RequestParam @Valid Double amountReal) {
        var quote = exchangeService.getQuote_External(LocalDateTime.now().toString());
        var converts = exchangeService.conversionCurrency(documentNumber, amountReal, quote);

        if (converts == null || converts < 0.01) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Convert fail.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Value converted.");

    }
}
