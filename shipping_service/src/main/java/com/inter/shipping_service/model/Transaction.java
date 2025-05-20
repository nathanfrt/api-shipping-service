package com.inter.shipping_service.model;

import com.inter.shipping_service.dto.TransactionDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double amount;
    private Double quote;
    private Double conversionAmount;

    private String transactionBy;
    private String transactionTo;

    private LocalDateTime createdAt;
    private Double limitDay;

    public Transaction(TransactionDto transactionDto) {
        BeanUtils.copyProperties(transactionDto, this);
    }
}
