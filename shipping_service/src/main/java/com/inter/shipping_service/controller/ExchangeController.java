package com.inter.shipping_service.controller;

import com.inter.shipping_service.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/exchange")
@Tag(name = "Câmbio", description = "Controlador para verificar o câmbio atual e comprar USD")
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/quote")
    @Operation(summary = "Consultar valor da cotação atual")
    public ResponseEntity<?> olindaAPI_External(@RequestParam String date){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dateFormat = LocalDate.parse(date, formatter);

            double result = exchangeService.getQuote_External(dateFormat);
            if (result > 0){
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quote not found");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when checking quote: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    @Operation(summary = "Converter valor BRL para USD a partir saldo bancário do usuário")
    public ResponseEntity<?> postTransaction(@RequestParam @Valid String documentNumber, @RequestParam @Valid Double amountReal) {
        try {
            var quote = exchangeService.getQuote_External(LocalDate.now());
            var converts = exchangeService.conversionCurrency(documentNumber, amountReal, quote);

            if (converts == null || converts < 0.01) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Convert fail.");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Value converted: " + converts);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
