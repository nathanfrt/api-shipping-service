package com.inter.shipping_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BalanceResponse {
    @Id
    private Long id;

    @NumberFormat(pattern = "#0.00")
    private Double balanceReal;

    @NumberFormat(pattern = "#0.00")
    private Double balanceDollar;
}
