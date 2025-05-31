package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.OlindaResponseDto;
import com.inter.shipping_service.dto.ValueDto;
import com.inter.shipping_service.exception.NotExist;
import com.inter.shipping_service.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        Double transferAmount = BigDecimal.valueOf(amount / quote)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        var user = userService.getUserByDocumentNumber(documentNumber);
        Double balanceDollar = userService.getBalanceDollarByDocumentNumber(documentNumber);

        user.setBalanceReal(balanceReal - amount);
        user.setBalanceDollar(balanceDollar + transferAmount);
        userService.save(user);

        return transferAmount;
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

            ResponseEntity<OlindaResponseDto> response = restTemplate.getForEntity(url, OlindaResponseDto.class);
            OlindaResponseDto body = response.getBody();

            if (body != null && body.value() != null && !body.value().isEmpty()) {
                ValueDto quote = body.value().getFirst();
                Double rawValue = quote.cotacaoCompra();

                Double roundedValue = BigDecimal.valueOf(rawValue)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

                return roundedValue;
            }
            throw new NotExist("Quote not found");

        } catch (Exception e) {
            throw new NotExist("Error when checking quote: " + e.getMessage());
        }
    }
}
