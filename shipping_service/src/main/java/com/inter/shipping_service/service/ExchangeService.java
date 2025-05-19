package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.ExchangeDto;
import com.inter.shipping_service.exception.InsufficientBalance;
import com.inter.shipping_service.exception.NotExist;
import com.inter.shipping_service.model.Exchange;
import com.inter.shipping_service.model.OlindaResponse;
import com.inter.shipping_service.model.Value;
import com.inter.shipping_service.repository.ExchangeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ExchangeService {

    private ExchangeRepository exchangeRepository;
    private UserService userService;

    // Conversão de dollar para Real
    public Double conversionCurrency(String documentNumber, Double amount, Double quote){
        userService.exceptionDocumentNumber(documentNumber);

        if (!existsBalanceToTransactionUSD(documentNumber,amount, quote)){
            throw new InsufficientBalance("Insufficient balance");
        }

        var balance = userService.getBalanceRealByDocumentNumber(documentNumber);
        Double transferAmount = balance / quote;

        var user = userService.getUserByDocumentNumber(documentNumber);
        user.setBalanceReal(user.getBalanceReal() - transferAmount);
        user.setBalanceDollar(user.getBalanceDollar() + amount);
        userService.save(user);

        return transferAmount;
    }

    // Verifica se existe valor para transf. USD
    public Boolean existsBalanceToTransactionUSD(String documentNumber, Double amount, Double quote){
        var balance = userService.getBalanceRealByDocumentNumber(documentNumber);
        return (balance / quote) >= amount;
    }

    // Formata data dentro do padrão desejado
    public String formatDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate data = LocalDate.parse(date, formatter);

        while (data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY) {
            data = data.minusDays(1);
        }
        return formatter.format(data);
    }

    public Double getQuote_External(String date){
        try {
            RestTemplate restTemplate = new RestTemplate();
            var dateFormat = formatDate(date);

            String url = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/" +
                    "CotacaoDolarDia(dataCotacao=@dataCotacao)?@dataCotacao='" + dateFormat + "'&$format=json";

            ResponseEntity<OlindaResponse> response = restTemplate.getForEntity(url, OlindaResponse.class);
            OlindaResponse body = response.getBody();

            if (body != null && body.getValue() != null && !body.getValue().isEmpty()) {
                Value quote = body.getValue().getFirst();
                return quote.getCotacaoCompra();
            }
            throw new NotExist("Quote not found");

        } catch (Exception e) {
            throw new NotExist("Error when checking quote: " + e.getMessage());
        }
    }
}
