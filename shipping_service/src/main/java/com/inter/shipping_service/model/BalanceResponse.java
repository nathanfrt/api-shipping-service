package com.inter.shipping_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BalanceResponse {
    @Id
    private Long id;
    private Double balanceReal;
    private Double balanceDolar;
}
