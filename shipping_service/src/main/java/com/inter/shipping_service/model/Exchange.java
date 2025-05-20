package com.inter.shipping_service.model;

import com.inter.shipping_service.dto.ExchangeDto;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.NumberFormat;

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

    @NumberFormat(pattern = "#0.00")
    private Double amount;

    @NumberFormat(pattern = "#0.00")
    private Double quote;

    @NumberFormat(pattern = "#0.00")
    private Double total;

    private LocalDateTime createdAt;
    private String documentNumber;

    public Exchange(ExchangeDto exchangeDto) {
        BeanUtils.copyProperties(exchangeDto, this);
    }
}
