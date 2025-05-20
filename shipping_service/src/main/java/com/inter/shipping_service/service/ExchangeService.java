package com.inter.shipping_service.service;

import com.inter.shipping_service.exception.InsufficientBalance;
import com.inter.shipping_service.exception.NotExist;
import com.inter.shipping_service.model.OlindaResponse;
import com.inter.shipping_service.model.Value;
import com.inter.shipping_service.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private UserService userService;

    public Double conversionCurrency(String documentNumber, Double amount, Double quote){
        userService.exceptionDocumentNumber(documentNumber);

        var balanceReal = userService.getBalanceRealByDocumentNumber(documentNumber);
        Double transferAmount = balanceReal / quote;

        var user = userService.getUserByDocumentNumber(documentNumber);
        Double balanceDollar = userService.getBalanceDollarByDocumentNumber(documentNumber);

        user.setBalanceReal(balanceReal - transferAmount);
        user.setBalanceDollar(balanceDollar + amount);
        userService.save(user);

        return transferAmount;
    }

    // Verifica se existe valor para transf. USD
    public Boolean existsBalanceToTransactionUSD(String documentNumber, Double amount, Double quote){
        Double balance = userService.getBalanceDollarByDocumentNumber(documentNumber);
        return (balance / quote) >= amount;
    }

    // Formata data dentro do padrão desejado
    public String formatDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(1);
        }

        return formatter.format(date);
    }

    public Double getQuote_External(LocalDate date){
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
