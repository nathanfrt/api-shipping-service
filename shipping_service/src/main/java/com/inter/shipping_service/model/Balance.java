package com.inter.shipping_service.model;

import com.inter.shipping_service.dto.BalanceDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "balance")
public class Balance {
    @Id
    private Long id;
    private Double amount;
    private TypeBalance typeBalance;
    private LocalDateTime createdAt;
    private String documentNumber;

    public Balance(BalanceDto balance){
        BeanUtils.copyProperties(balance, this);
    }

}

