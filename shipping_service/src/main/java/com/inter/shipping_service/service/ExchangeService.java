package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.ExchangeDto;
import com.inter.shipping_service.exception.InsufficientBalance;
import com.inter.shipping_service.model.Exchange;
import com.inter.shipping_service.repository.ExchangeRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ExchangeService {

    private ExchangeRepository exchangeRepository;
    private UserService userService;

    // Conversão de dolar para Real
    public Exchange conversionCurrency(ExchangeDto exchangeDto, Double quote){
        userService.exceptionDocumentNumber(exchangeDto.documentNumber());

        if (!existsBalanceToTransactionUSD(exchangeDto.documentNumber(),exchangeDto.amount(), quote)){
            throw new InsufficientBalance("Insufficient balance");
        }

        var balance = userService.getBalanceRealByDocumentNumber(exchangeDto.documentNumber());
        Double transferAmount = balance / quote;

        var user = userService.getUserByDocumentNumber(exchangeDto.documentNumber());
        user.setBalanceReal(user.getBalanceReal() - transferAmount);
        user.setBalanceDollar(user.getBalanceDollar() + exchangeDto.amount());
        userService.save(user);

        Exchange exchange = new Exchange(exchangeDto);
        exchange.setTotal(transferAmount);
        exchange.setQuote(quote);
        exchange.setCreatedAt(LocalDateTime.now());

        return exchangeRepository.save(exchange);
    }

    // Verifica se existe valor para transf. USD
    public Boolean existsBalanceToTransactionUSD(String documentNumber, Double amount, Double quote){
        var balance = userService.getBalanceRealByDocumentNumber(documentNumber);
        return (balance / quote) >= amount;
    }

    public String formatDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate data = LocalDate.parse(date, formatter);

        while (data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY) {
            data = data.minusDays(1);
        }
        return formatter.format(data);
    }

}
