package com.inter.shipping_service.model;

import com.inter.shipping_service.dto.ExchangeDto;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exchange")
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private Double amount;
    private Double quote;
    private Double total;

    private LocalDateTime createdAt;
    private String documentNumber;

    public Exchange(ExchangeDto exchangeDto) {
        BeanUtils.copyProperties(exchangeDto, this);
    }
}
