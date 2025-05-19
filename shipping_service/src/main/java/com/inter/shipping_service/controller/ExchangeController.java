package com.inter.shipping_service.controller;

import com.inter.shipping_service.model.OlindaResponse;
import com.inter.shipping_service.model.Value;
import com.inter.shipping_service.repository.BalanceRepository;
import com.inter.shipping_service.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/quote/{date}/")
    public ResponseEntity<?> olindaAPI_External(@PathVariable String date){
        try {
            RestTemplate restTemplate = new RestTemplate();
            var dateFormat = exchangeService.formatDate(date);

            String url = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/" +
                    "CotacaoDolarDia(dataCotacao=@dataCotacao)?@dataCotacao='" + dateFormat + "'&$format=json";

            ResponseEntity<OlindaResponse> response = restTemplate.getForEntity(url, OlindaResponse.class);
            OlindaResponse body = response.getBody();

            if (body != null && body.getValue() != null && !body.getValue().isEmpty()) {
                Value quote = body.getValue().getFirst();
                return ResponseEntity.status(HttpStatus.OK).body(quote.getCotacaoCompra());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quote not found");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when checking quote: " + e.getMessage());
        }
    }
}
