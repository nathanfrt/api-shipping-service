package com.inter.shipping_service.model;

import com.inter.shipping_service.dto.BalanceDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "balance")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NumberFormat(pattern = "#0.00")
    private Double amount;

    private TypeBalance typeBalance;
    private LocalDateTime createdAt;
    private String documentNumber;

    public Balance(BalanceDto balance){
        BeanUtils.copyProperties(balance, this);
    }

}

