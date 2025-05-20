package com.inter.shipping_service.model;

import com.inter.shipping_service.dto.TransactionDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
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

    @NumberFormat(pattern = "#0.00")
    private Double amount;

    @NumberFormat(pattern = "#0.00")
    private Double quote;

    @NumberFormat(pattern = "#0.00")
    private Double conversionAmount;

    private String transactionBy;
    private String transactionTo;

    private LocalDate createdAt;
    private Double limitDay;
    private Double limitUsed;

    public Transaction(TransactionDto transactionDto) {
        BeanUtils.copyProperties(transactionDto, this);
    }
}
